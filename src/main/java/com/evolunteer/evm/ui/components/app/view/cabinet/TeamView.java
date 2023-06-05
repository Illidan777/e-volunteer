package com.evolunteer.evm.ui.components.app.view.cabinet;

import com.evolunteer.evm.backend.service.file_management.FileService;
import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.dto.fund_management.FundTeamRequestDto;
import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestType;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.layout.navigation.ParentNavigationLayout;
import com.evolunteer.evm.ui.components.general.button.AcceptButton;
import com.evolunteer.evm.ui.components.general.button.DeleteButton;
import com.evolunteer.evm.ui.components.general.button.RejectButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.header.H4Header;
import com.evolunteer.evm.ui.components.general.image.ImageAvatar;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.Route;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.SPACE;

@Route(value = RouteUtils.TEAM_ROUTE, layout = ParentNavigationLayout.class)
public class TeamView extends VerticalLayout {

    private final Locale locale;
    private final MessageSource messageSource;
    private final UserService userService;
    private final FundService fundService;
    private final FileService fileService;


    public TeamView(MessageSource messageSource, UserService userService, FundService fundService, FileService fileService) {
        this.setSizeFull();

        this.messageSource = messageSource;
        this.locale = LocalizationUtils.getLocale();
        this.userService = userService;
        this.fundService = fundService;
        this.fileService = fileService;

        final BaseUserDto contextUser = userService.getContextUser();
        final BaseFundDto contextUserFund = contextUser.getFund();
        if (Objects.isNull(contextUserFund)) {
            NotificationFactory.error("On development!").open();
        } else {
            final FundDtoFull fundDtoFull = fundService.getFundById(contextUserFund.getId());

            final SplitLayout splitLayout = new SplitLayout(
                    this.teamLayout(contextUser, fundDtoFull),
                    this.applicationsLayout(fundDtoFull));
            splitLayout.setSizeFull();

            this.add(splitLayout);
        }
    }

    private VerticalLayout applicationsLayout(final FundDtoFull fundDtoFull) {

        final String noApplicationsText = messageSource.getMessage(LocalizationUtils.UI.TeamView.NO_APPLICATIONS_TEXT, null, locale);
        final VerticalLayout applicationLayout = new VerticalLayout();
        applicationLayout.setSpacing(true);
        applicationLayout.setPadding(true);

        final VerticalLayout inviteMemberLayout = this.inviteMemberLayout(fundDtoFull);

        final VerticalLayout membersApplicationLayout = new VerticalLayout(new H3Header(messageSource, locale, LocalizationUtils.UI.TeamView.APPLICATIONS_HEADER_TEXT));

        final Set<FundTeamRequestDto> fundTeamRequests = fundDtoFull.getRequests().stream()
                .filter(request -> request.getFundRequestType().isUserRequest())
                .filter(request -> request.getStatus().isNew())
                .collect(Collectors.toSet());
        if (fundTeamRequests.isEmpty()) {
            final Div noApplicationsDiv = new Div();
            noApplicationsDiv.setWidth("100%");
            noApplicationsDiv.getStyle().set("text-align", "center");
            noApplicationsDiv.setText(noApplicationsText);
            membersApplicationLayout.add(
                    noApplicationsDiv
            );
        } else {
            fundTeamRequests.forEach(request -> {
                final Div teamMemberDiv = this.teamMemberDiv(request.getUser());
                final AcceptButton acceptButton = new AcceptButton(messageSource, locale, event -> {
                    fundService.processFundTeamRequest(request.getId(), true);
                    UI.getCurrent().getPage().reload();
                });
                final RejectButton rejectButton = new RejectButton(messageSource, locale, event -> {
                    fundService.processFundTeamRequest(request.getId(), false);
                    UI.getCurrent().getPage().reload();
                });
                final HorizontalLayout buttonLayout = new HorizontalLayout(acceptButton, rejectButton);
                buttonLayout.setAlignItems(Alignment.END);
                buttonLayout.setJustifyContentMode(JustifyContentMode.END);

                teamMemberDiv.add(buttonLayout);
                membersApplicationLayout.add(teamMemberDiv);
            });
        }

        applicationLayout.add(
                inviteMemberLayout,
                new Hr(),
                membersApplicationLayout
        );

        return applicationLayout;
    }

    private VerticalLayout inviteMemberLayout(final FundDtoFull fundDtoFull) {
        final String inviteDescriptionText = messageSource.getMessage(LocalizationUtils.UI.TeamView.INVITE_MEMBER_DESCRIPTION_TEXT, null, locale);
        final String usersSelectText = messageSource.getMessage(LocalizationUtils.UI.TeamView.USERS_SELECT_TEXT, null, locale);
        final String inviteButtonText = messageSource.getMessage(LocalizationUtils.UI.TeamView.INVITE_MEMBER_BUTTON_TEXT, null, locale);
        final String noUserSelectedToInviteText = messageSource.getMessage(LocalizationUtils.UI.TeamView.NO_USER_SELECTED_TO_INVITE_TEXT, null, locale);
        final String invitationSuccessfullySentText = messageSource.getMessage(LocalizationUtils.UI.TeamView.INVITATION_SUCCESSFULLY_SENT_TEXT, null, locale);
        final String invitationInProgressText = messageSource.getMessage(LocalizationUtils.UI.TeamView.INVITATION_IN_PROGRESS_TEXT, null, locale);

        final VerticalLayout inviteMemberLayout = new VerticalLayout();

        final ComboBox<BaseUserDto> usersComboBox = new ComboBox<>(usersSelectText);
        usersComboBox.setWidthFull();
        usersComboBox.setAllowCustomValue(false);
        usersComboBox.setHelperComponent(new Icon(VaadinIcon.SEARCH));
        usersComboBox.setItems(userService.getAllUsers().stream()
                .filter(userDto -> Objects.isNull(userDto.getFund()))
                .filter(userDto -> fundDtoFull.getRequests().stream()
                        .filter(request -> request.getStatus().isNew())
                        .map(FundTeamRequestDto::getUser)
                        .noneMatch(requestUser -> userDto.getId().equals(requestUser.getId())))
                .collect(Collectors.toSet()));
        usersComboBox.setItemLabelGenerator(BaseUserDto::getFullNameWithUsername);

        final Button inviteButton = new Button(inviteButtonText, event -> {
            final BaseUserDto selectedUser = usersComboBox.getValue();
            if (Objects.isNull(selectedUser)) {
                NotificationFactory.error(noUserSelectedToInviteText).open();
            } else {
                final Long userId = selectedUser.getId();
                final Long fundId = fundDtoFull.getId();
                final FundRequestType type = FundRequestType.FUND_INVITATION;

                final Optional<FundTeamRequestDto> fundTeamRequestDto = fundService.getFundTeamRequest(userId, fundId, type);
                if (fundTeamRequestDto.isPresent()) {
                    final FundTeamRequestDto fundTeamRequest = fundTeamRequestDto.get();
                    if (fundTeamRequest.getStatus().isNew()) {
                        NotificationFactory.error(invitationInProgressText).open();
                        return;
                    }
                }
                NotificationFactory.success(invitationSuccessfullySentText).open();
                fundService.createFundTeamRequest(userId, fundId, type);
            }
        });
        inviteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        final HorizontalLayout buttonLayout = new HorizontalLayout(inviteButton);
        buttonLayout.setAlignItems(Alignment.END);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        inviteMemberLayout.add(
                new H3Header(messageSource, locale, LocalizationUtils.UI.TeamView.INVITE_MEMBER_HEADER_TEXT),
                new Span(inviteDescriptionText),
                usersComboBox,
                buttonLayout
        );
        return inviteMemberLayout;
    }

    private VerticalLayout teamLayout(final BaseUserDto contextUser, final FundDtoFull fundDtoFull) {
        final VerticalLayout teamVerticalLayout = new VerticalLayout();
        fundDtoFull.getEmployees().forEach(employee -> {
            final Div teamMemberDiv = this.teamMemberDiv(employee);
            if (fundDtoFull.getCreatedBy().getId().equals(contextUser.getId())) {
                final DeleteButton removeMember = new DeleteButton(messageSource, locale, event -> {
                    fundService.deleteMemberFromFund(employee.getId());
                    UI.getCurrent().getPage().reload();
                });
                final HorizontalLayout buttonLayout = new HorizontalLayout(removeMember);
                buttonLayout.setAlignItems(Alignment.START);
                buttonLayout.setJustifyContentMode(JustifyContentMode.START);

                if (!employee.getId().equals(contextUser.getId())) {
                    teamMemberDiv.add(buttonLayout);
                }
            }
            teamVerticalLayout.add(teamMemberDiv);
        });
        return teamVerticalLayout;
    }

    private Div teamMemberDiv(final BaseUserDto baseUserDto) {
        final String moreInfoDetails = messageSource.getMessage(LocalizationUtils.UI.CommonText.MORE_INFO_DETAILS_TEXT, null, locale);
        final String phoneInfoText = messageSource.getMessage(LocalizationUtils.UI.TeamView.PHONE_FIELD_TEXT, null, locale);
        final String emailInfoText = messageSource.getMessage(LocalizationUtils.UI.TeamView.EMAIL_FIELD_TEXT, null, locale);

        final HorizontalLayout memberLayout = new HorizontalLayout();
        memberLayout.setAlignItems(FlexComponent.Alignment.START);
        memberLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        final FileMetaDataDto userPicture = baseUserDto.getPicture();
        String pictureFileCode = null;
        if (Objects.nonNull(userPicture)) {
            pictureFileCode = userPicture.getCode();
        }
        final String username = baseUserDto.getAccountDetails().getUsername();
        final H4Header fullNameHeader = new H4Header(baseUserDto.getFullNameWithUsername());
        final ImageAvatar userAvatar = new ImageAvatar(fileService, pictureFileCode, username);
        userAvatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);

        final Details memberDetails = new Details(moreInfoDetails);
        final Span phoneSpan = new Span(phoneInfoText + SPACE + baseUserDto.getPhone());
        final Span emailSpan = new Span(emailInfoText + SPACE + baseUserDto.getEmail());
        final VerticalLayout detailsContent = new VerticalLayout(phoneSpan, emailSpan);
        detailsContent.setSpacing(false);
        detailsContent.setPadding(false);
        memberDetails.addContent(detailsContent);

        final VerticalLayout userInfo = new VerticalLayout(fullNameHeader, memberDetails);

        memberLayout.add(userAvatar, userInfo);
        return new Div(memberLayout);
    }
}
