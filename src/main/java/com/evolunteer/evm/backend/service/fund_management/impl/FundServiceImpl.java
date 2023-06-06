package com.evolunteer.evm.backend.service.fund_management.impl;

import com.evolunteer.evm.backend.repository.fund_management.*;
import com.evolunteer.evm.backend.service.address_management.AddressService;
import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.backend.service.notification_management.sender.NotificationService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.fund_management.*;
import com.evolunteer.evm.common.domain.dto.notification_management.NotificationRequest;
import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDtoFull;
import com.evolunteer.evm.common.domain.entity.address_management.Address;
import com.evolunteer.evm.common.domain.entity.fund_management.*;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.enums.fund_management.FundActivityCategory;
import com.evolunteer.evm.common.domain.enums.fund_management.FundHelpRequestStatus;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestStatus;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestType;
import com.evolunteer.evm.common.domain.enums.notification_management.NotificationProviderType;
import com.evolunteer.evm.common.domain.exception.common.ResourceNotFoundException;
import com.evolunteer.evm.common.domain.request.fund_management.CreateFundRequest;
import com.evolunteer.evm.common.domain.request.fund_management.UpdateFundRequest;
import com.evolunteer.evm.common.mapper.address_management.AddressMapper;
import com.evolunteer.evm.common.mapper.fund_management.FundMapper;
import com.evolunteer.evm.common.mapper.user_management.UserMapper;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.EmailNotification.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;
    private final FundRequisiteRepository fundRequisiteRepository;
    private final FundTeamRequestRepository fundTeamRequestRepository;
    private final FundHelpRequestRepository fundHelpRequestRepository;
    private final FundHelpRequestExecutorRepository requestExecutorRepository;
    private final FundMapper fundMapper;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;
    private final AddressService addressService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final MessageSource messageSource;

    @Transactional
    @Override
    public FundDtoFull createFund(final CreateFundRequest createFundRequest) {
        ValidationUtils.validate(createFundRequest);
        createFundRequest.getRequisites().forEach(ValidationUtils::validate);

        final BaseUserDto contextUser = userService.getContextUser();
        final Address address = addressMapper.mapAddressDtoToAddress(addressService.create(createFundRequest.getAddress()));
        final Fund mappedFund = fundMapper.mapCreateFundRequestToFund(createFundRequest);
        mappedFund.setAddress(address);
        mappedFund.setCreatedBy(userMapper.mapUserDtoToUser(contextUser));
        final Fund savedFund = fundRepository.save(mappedFund);

        createFundRequest.getRequisites().forEach(fundRequisiteDto -> {
            saveRequisite(savedFund, fundRequisiteDto);
        });
        final FundDtoFull fund = fundMapper.mapFundToFundDtoFull(this.findFundById(savedFund.getId()));
        userService.setFundToUser(contextUser.getId(), fund);
        return fund;
    }

    @Override
    public FundDtoFull getFundById(final Long fundId) {
        return fundMapper.mapFundToFundDtoFull(this.findFundById(fundId));
    }

    @Transactional
    @Override
    public void deleteRequisiteById(final Long requisiteId) {
        fundRequisiteRepository.deleteById(requisiteId);
    }

    @Transactional
    @Override
    public FundRequisiteDto addOrUpdateRequisite(final Long fundId, final FundRequisiteDto fundRequisiteDto) {
        ValidationUtils.validate(fundRequisiteDto);
        final Fund fund = this.findFundById(fundId);
        return this.saveRequisite(fund, fundRequisiteDto);
    }

    @Transactional
    @Override
    public FundDtoFull updateFund(final UpdateFundRequest updateFundRequest) {
        ValidationUtils.validate(updateFundRequest);
        final Fund mappedFund = fundMapper.mapUpdateFundRequesToFund(updateFundRequest, this.findFundById(updateFundRequest.getId()));
        return fundMapper.mapFundToFundDtoFull(fundRepository.save(mappedFund));
    }

    @Override
    public void deleteMemberFromFund(final Long userId) {
        userService.setFundToUser(userId, null);
    }

    @Transactional
    @Override
    public void createFundTeamRequest(final Long userId, final Long fundId, final FundRequestType type) {
        final UserDtoFull userDto = userService.getById(userId);
        final Fund fund = this.findFundById(fundId);

        final Optional<FundTeamRequest> optionalFundTeamRequest = fundTeamRequestRepository.findByUser_IdAndFund_IdAndFundRequestType(userId, fundId, type);
        if(optionalFundTeamRequest.isPresent()) {
            final FundTeamRequest fundTeamRequest = optionalFundTeamRequest.get();
            fundTeamRequest.setStatus(FundRequestStatus.NEW);
            fundTeamRequestRepository.save(fundTeamRequest);
        }else {
            final FundTeamRequest newRequest = new FundTeamRequest();
            newRequest.setFundRequestType(type);
            newRequest.setUser(userMapper.mapUserDtoToUser(userDto));
            newRequest.setFund(fund);
            newRequest.setStatus(FundRequestStatus.NEW);
            fundTeamRequestRepository.save(newRequest);
        }

        if (type.equals(FundRequestType.FUND_INVITATION)) {
            final String invitationSubject = messageSource.getMessage(USER_FUND_INVITATION_NOTIFICATION_SUBJECT,
                    null, LocalizationUtils.getLocale());
            final String invitationPattern = messageSource.getMessage(USER_FUND_INVITATION_NOTIFICATION_PATTERN,
                    new String[]{userDto.getFullName(), fund.getName()}, LocalizationUtils.getLocale());
            final NotificationRequest notificationRequest = NotificationRequest.of(
                    invitationPattern,
                    invitationSubject,
                    NotificationProviderType.EMAIL,
                    Set.of(userDto.getEmail())
            );
            notificationService.send(notificationRequest);
        }
    }

    @Transactional
    @Override
    public void processFundTeamRequest(final Long requestId, final boolean isAccept) {
        final FundTeamRequest request = this.findFundTeamRequestById(requestId);
        final User user = request.getUser();
        final Fund fund = request.getFund();
        final FundRequestType type = request.getFundRequestType();
        if (isAccept) {
            request.setStatus(FundRequestStatus.ACCEPTED);
            userService.setFundToUser(user.getId(), this.getFundById(fund.getId()));
            if (type.equals(FundRequestType.FUND_INVITATION)) {
                final String invitationSubject = messageSource.getMessage(SUCCESSFULLY_ACCEPTED_MEMBER_OF_FUND_SUBJECT,
                        null, LocalizationUtils.getLocale());
                final String invitationPattern = messageSource.getMessage(SUCCESSFULLY_ACCEPTED_MEMBER_OF_FUND_PATTERN,
                        new String[]{user.getFullName(), fund.getName()}, LocalizationUtils.getLocale());
                final NotificationRequest notificationRequest = NotificationRequest.of(
                        invitationPattern,
                        invitationSubject,
                        NotificationProviderType.EMAIL,
                        Set.of(user.getEmail())
                );
                notificationService.send(notificationRequest);
            }
        } else {
            request.setStatus(FundRequestStatus.REJECTED);
        }
        fundTeamRequestRepository.save(request);
    }

    @Override
    public Set<BaseFundDto> getAllFunds() {
        return fundRepository.findAll().stream()
                .map(fundMapper::mapFundToBaseFundDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<FundTeamRequestDto> getFundTeamRequest(final Long userId, final Long fundId, final FundRequestType type) {
        return fundTeamRequestRepository.findByUser_IdAndFund_IdAndFundRequestType(userId, fundId, type)
                .map(fundMapper::mapFundTeamRequestToFundTeamRequestDto);
    }

    @Override
    public Set<FundDtoFull> findFundByNameAndCategories(final String name, final Set<FundActivityCategory> categories) {
        Stream<Fund> fundStream = fundRepository.findAll().stream();
        if(!StringUtils.isBlank(name)) {
            final String normalizedFundNameArg = StringUtils.normalizeSpace(name);
            fundStream = fundStream.filter(fund -> StringUtils.containsIgnoreCase(fund.getName(), normalizedFundNameArg));
        }
        if(Objects.nonNull(categories) && !categories.isEmpty()) {
            fundStream = fundStream.filter(fund -> fund.getCategories().stream().anyMatch(categories::contains));
        }
        return fundStream.map(fundMapper::mapFundToFundDtoFull).collect(Collectors.toSet());
    }

    @Override
    public void createFundHelpRequest(final Long fundId, final FundHelpRequestDto fundHelpRequestDto) {
        final Fund fund = this.findFundById(fundId);

        final FundHelpRequestStatus status = FundHelpRequestStatus.NEW;

        final HelpRequestExecutor savedExecutor = requestExecutorRepository.save(fundMapper.mapHelpExecutorDtoToHelpExecutor(fundHelpRequestDto.getExecutor()));
        final FundHelpRequest helpRequest = fundMapper.mapFundHelpRequestDtoToFundHelpRequest(fundHelpRequestDto);
        helpRequest.setExecutor(savedExecutor);
        helpRequest.setStatus(status);
        helpRequest.setFund(fund);
        fundHelpRequestRepository.save(helpRequest);

        final NotificationRequest notificationRequest = NotificationRequest.of(
                messageSource.getMessage(status.getLocalizedMessage(), new String[]{fundHelpRequestDto.getNumber()}, LocalizationUtils.getLocale()),
                messageSource.getMessage(NEW_FUND_HELP_REQUEST_SUBJECT, null, LocalizationUtils.getLocale()),
                NotificationProviderType.EMAIL,
                Set.of(savedExecutor.getEmail())
        );
        notificationService.send(notificationRequest);
    }

    @Override
    public Optional<FundHelpRequestDto> getFundHelpRequestByNumber(final String number) {
        return fundHelpRequestRepository.findByNumber(number).map(fundMapper::mapFundHelpRequestToFundHelpRequestDto);
    }

    @Transactional
    @Override
    public void updateFundHelpRequestStatus(final Long requestId, final FundHelpRequestStatus status) {
        final FundHelpRequest request = this.findFundHelpRequestById(requestId);
        request.setStatus(status);
        fundHelpRequestRepository.save(request);

        final NotificationRequest notificationRequest = NotificationRequest.of(
                messageSource.getMessage(status.getLocalizedMessage(), new String[]{request.getNumber()}, LocalizationUtils.getLocale()),
                messageSource.getMessage(CHANGE_FUND_HELP_REQUEST_STATUS_SUBJECT, null, LocalizationUtils.getLocale()),
                NotificationProviderType.EMAIL,
                Set.of(request.getExecutor().getEmail())
        );
        notificationService.send(notificationRequest);
    }

    private FundRequisiteDto saveRequisite(final Fund savedFund, final FundRequisiteDto fundRequisiteDto) {
        final FundRequisite mappedRequisite = fundMapper.mapFundRequisiteDtoToFundRequisite(fundRequisiteDto);
        mappedRequisite.setFund(savedFund);
        return fundMapper.mapFundRequisiteToFundRequisiteDto(fundRequisiteRepository.save(mappedRequisite));
    }

    private FundHelpRequest findFundHelpRequestById(final Long requestId) {
        Assert.notNull(requestId, "Unable to get fund help request by id. Id is null!");
        return fundHelpRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Fund help request by %s id does not exist", requestId)));
    }

    private FundTeamRequest findFundTeamRequestById(final Long requestId) {
        Assert.notNull(requestId, "Unable to get fund team request by id. Id is null!");
        return fundTeamRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Fund team request by %s id does not exist", requestId)));
    }

    private Fund findFundById(final Long fundId) {
        Assert.notNull(fundId, "Unable to get fund by id. Id is null!");
        return fundRepository.findById(fundId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Fund by %s id does not exist", fundId)));
    }
}
