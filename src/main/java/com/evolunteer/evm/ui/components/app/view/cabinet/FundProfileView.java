package com.evolunteer.evm.ui.components.app.view.cabinet;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;
import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.dto.fund_management.FundRequisiteDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundTeamRequestDto;
import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDtoFull;
import com.evolunteer.evm.common.domain.enums.fund_management.FundRequestType;
import com.evolunteer.evm.common.domain.request.fund_management.UpdateFundRequest;
import com.evolunteer.evm.common.mapper.fund_management.FundMapper;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.dialog.fund_management.FundRegistrationDialog;
import com.evolunteer.evm.ui.components.app.layout.form.address_management.AddressFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundCommunicationFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundMainInfoFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundRequisiteBlockFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundRequisiteContainerFormLayout;
import com.evolunteer.evm.ui.components.app.layout.navigation.ParentNavigationLayout;
import com.evolunteer.evm.ui.components.general.button.*;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.header.H4Header;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.context.MessageSource;

import java.util.*;
import java.util.stream.Collectors;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundProfileView.*;
import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.SPACE;

@Route(value = RouteUtils.FUND_PROFILE_ROUTE, layout = ParentNavigationLayout.class)
public class FundProfileView extends VerticalLayout {

    private final Locale locale;
    private final MessageSource messageSource;
    private final FundService fundService;
    private final Binder<UpdateFundRequest> updateFundRequestBinder;
    private final Binder<AddressDto> addressDtoBinder;
    private final Map<Long, Binder<FundRequisiteDto>> requisiteBinders;
    private UpdateFundRequest updateFundRequest;
    private AddressDto address;
    private UserDtoFull contextUser;

    public FundProfileView(MessageSource messageSource, UserService userService, FundService fundService, FundMapper fundMapper) {
        this.locale = LocalizationUtils.getLocale();
        this.messageSource = messageSource;
        this.fundService = fundService;
        this.updateFundRequestBinder = new Binder<>();
        this.addressDtoBinder = new Binder<>();
        this.requisiteBinders = new HashMap<>();
        final BaseUserDto baseContextUser = userService.getContextUser();
        this.contextUser = userService.getById(baseContextUser.getId());

        final BaseFundDto userFund = baseContextUser.getFund();
        if (Objects.isNull(userFund)) {
            this.createIntroduction();
        } else {
            final FundDtoFull fund = fundService.getFundById(userFund.getId());
            this.updateFundRequest = fundMapper.mapFundDtoFullToUpdateFundRequest(fund);
            this.address = fund.getAddress();

            final FundRequisiteContainerFormLayout requisiteContainerFormLayout =
                    new FundRequisiteContainerFormLayout(messageSource, locale, this.addRequisiteButtonClickListener(fund.getId()));
            fund.getRequisites().forEach(requisite -> {
                this.addFundRequisiteBlockFormLayout(fund, requisiteContainerFormLayout, requisite);
            });

            final SaveButton saveButton = new SaveButton(messageSource, locale, this.updateFund());
            final HorizontalLayout buttonLayout = new HorizontalLayout(saveButton);
            buttonLayout.setAlignItems(Alignment.START);
            buttonLayout.setJustifyContentMode(JustifyContentMode.START);

            final FormLayout updatingFundForm = new FormLayout();
            updatingFundForm.add(
                    new H3Header(messageSource, locale, HEADER_TEXT),
                    new Hr(),
                    new FundMainInfoFormLayout<>(messageSource, locale, updateFundRequestBinder),
                    new Hr(),
                    new FundCommunicationFormLayout<>(messageSource, locale, updateFundRequestBinder),
                    new Hr(),
                    new AddressFormLayout(messageSource, locale, addressDtoBinder),
                    new Hr(),
                    buttonLayout,
                    requisiteContainerFormLayout

            );
            updatingFundForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
            add(updatingFundForm);

            fund.getRequisites().forEach(fundRequisiteDto -> {
                requisiteBinders.get(fundRequisiteDto.getId()).readBean(fundRequisiteDto);
            });
            updateFundRequestBinder.readBean(updateFundRequest);
            addressDtoBinder.readBean(address);
        }
    }

    private void addFundRequisiteBlockFormLayout(final FundDtoFull fund,
                                                 final FundRequisiteContainerFormLayout requisiteContainerFormLayout,
                                                 final FundRequisiteDto requisite) {
        final Binder<FundRequisiteDto> fundRequisiteDtoBinder = new Binder<>();
        this.requisiteBinders.put(requisite.getId(), fundRequisiteDtoBinder);

        final FundRequisiteBlockFormLayout requisiteBlockFormLayout = new FundRequisiteBlockFormLayout(messageSource, locale, fundRequisiteDtoBinder);
        final DeleteButton removeRequisiteBlockButton = new DeleteButton(messageSource, locale, deleteEvent -> {
            if (requisiteBlockFormLayout.isAttached()) {
                requisiteContainerFormLayout.remove(requisiteBlockFormLayout);
                this.requisiteBinders.remove(requisite.getId());
                fundService.deleteRequisiteById(requisite.getId());
                UI.getCurrent().getPage().reload();
            }
        });
        final SaveButton saveRequisiteBlockButton = new SaveButton(messageSource, locale, deleteEvent -> {
            if (fundRequisiteDtoBinder.writeBeanIfValid(requisite)) {
                fundService.addOrUpdateRequisite(fund.getId(), requisite);
                UI.getCurrent().getPage().reload();
            }
        });
        requisiteBlockFormLayout.add(saveRequisiteBlockButton, removeRequisiteBlockButton);
        requisiteContainerFormLayout.add(requisiteBlockFormLayout);
    }

    private void createIntroduction() {
        final SplitLayout splitLayout = new SplitLayout(this.getIntroductionLayout(), this.invitationLayout());
        splitLayout.setSizeFull();
        this.add(splitLayout);
    }

    private VerticalLayout applyForParticipationLayout() {
        final String applyForParticipationText = messageSource.getMessage(APPLY_FOR_PARTICIPATION_DESCRIPTION_HEADER_TEXT, null, locale);
        final String usersSelectText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.FUND_SELECT_TEXT, null, locale);
        final String noFundSelectedToApplyForText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.NO_FUND_SELECTED_TO_APPLY_TEXT, null, locale);
        final String applicationSuccessfullySentText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.APPLICATION_SUCCESSFULLY_SENT_TEXT, null, locale);
        final String applicationInProgressText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.APPLICATION_IN_PROGRESS_TEXT, null, locale);

        final VerticalLayout applyForLayout = new VerticalLayout();

        final ComboBox<BaseFundDto> fundComboBox = new ComboBox<>(usersSelectText);
        fundComboBox.setWidthFull();
        fundComboBox.setAllowCustomValue(false);
        fundComboBox.setHelperComponent(new Icon(VaadinIcon.SEARCH));
        fundComboBox.setItems(fundService.getAllFunds().stream()
                .filter(fund -> contextUser.getInvitations().stream()
                        .filter(invitation -> invitation.getStatus().isNew())
                        .noneMatch(invitation -> invitation.getFund().getId().equals(fund.getId())))
                .collect(Collectors.toSet()));
        fundComboBox.setItemLabelGenerator(BaseFundDto::getName);

        final SendButton inviteButton = new SendButton(messageSource, locale, event -> {
            final BaseFundDto selectedFund = fundComboBox.getValue();
            if (Objects.isNull(selectedFund)) {
                NotificationFactory.error(noFundSelectedToApplyForText).open();
            } else {
                final Long userId = contextUser.getId();
                final Long fundId = selectedFund.getId();
                final FundRequestType type = FundRequestType.USER_REQUEST;

                final Optional<FundTeamRequestDto> fundTeamRequestDto = fundService.getFundTeamRequest(userId, fundId, type);
                if(fundTeamRequestDto.isPresent()) {
                    final FundTeamRequestDto fundTeamRequest = fundTeamRequestDto.get();
                    if(fundTeamRequest.getStatus().isNew()) {
                        NotificationFactory.error(applicationInProgressText).open();
                        return;
                    }
                }
                fundService.createFundTeamRequest(contextUser.getId(), selectedFund.getId(), FundRequestType.USER_REQUEST);
                NotificationFactory.success(applicationSuccessfullySentText).open();
            }
        });
        inviteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        final HorizontalLayout buttonLayout = new HorizontalLayout(inviteButton);
        buttonLayout.setAlignItems(Alignment.END);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        applyForLayout.add(
                new H3Header(messageSource, locale, LocalizationUtils.UI.FundProfileView.APPLY_FOR_PARTICIPATION_HEADER_TEXT),
                new Span(applyForParticipationText),
                fundComboBox,
                buttonLayout
        );
        return applyForLayout;
    }

    private VerticalLayout invitationLayout() {
        final String noInvitationsText = messageSource.getMessage(NO_INVITATIONS_TEXT, null, locale);

        final VerticalLayout mainInvitationLayout = new VerticalLayout();
        mainInvitationLayout.setSpacing(true);
        mainInvitationLayout.setPadding(true);

        final VerticalLayout invitationsLayout = new VerticalLayout(new H3Header(messageSource, locale, INVITATIONS_HEADER_TEXT));
        final Set<FundTeamRequestDto> invitations = contextUser.getInvitations().stream()
                .filter(request -> request.getFundRequestType().isFundInvitation())
                .filter(request -> request.getStatus().isNew())
                .collect(Collectors.toSet());
        if (invitations.isEmpty()) {
            final Div noInvitationsDiv = new Div();
            noInvitationsDiv.setWidth("100%");
            noInvitationsDiv.getStyle().set("text-align", "center");
            noInvitationsDiv.setText(noInvitationsText);
            invitationsLayout.add(
                    noInvitationsDiv
            );
        } else {
            invitations.stream()
                    .filter(request -> request.getFundRequestType().isFundInvitation())
                    .filter(request -> request.getStatus().isNew())
                    .forEach(request -> {
                        final Div invitationDiv = this.fundInvitationDiv(request.getFund());
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

                        invitationDiv.add(buttonLayout);
                        invitationsLayout.add(invitationDiv);
                    });
        }
        mainInvitationLayout.add(
                this.applyForParticipationLayout(),
                new Hr(),
                invitationsLayout
        );

        return mainInvitationLayout;
    }

    private Div fundInvitationDiv(final BaseFundDto baseFundDto) {
        final String moreInfoDetails = messageSource.getMessage(LocalizationUtils.UI.CommonText.MORE_INFO_DETAILS_TEXT, null, locale);
        final String phoneInfoText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.PHONE_FIELD_TEXT, null, locale);
        final String emailInfoText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.EMAIL_FIELD_TEXT, null, locale);
        final String descriptionInfoText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.DESCRIPTION_FIELD_TEXT, null, locale);

        final HorizontalLayout memberLayout = new HorizontalLayout();
        memberLayout.setAlignItems(FlexComponent.Alignment.START);
        memberLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        final H4Header fundNameHeader = new H4Header(baseFundDto.getName());

        final Details fundDetails = new Details(moreInfoDetails);
        final Span phoneSpan = new Span(phoneInfoText + SPACE + baseFundDto.getPhone());
        final Span emailSpan = new Span(emailInfoText + SPACE + baseFundDto.getEmail());
        final Span descriptionSpan = new Span(descriptionInfoText + SPACE + baseFundDto.getDescription());
        final VerticalLayout detailsContent = new VerticalLayout(phoneSpan, emailSpan, descriptionSpan);
        detailsContent.setSpacing(false);
        detailsContent.setPadding(false);
        fundDetails.addContent(detailsContent);

        memberLayout.add(fundNameHeader, fundDetails);
        return new Div(memberLayout);
    }

    private VerticalLayout getIntroductionLayout() {
        final String createFundButtonText = messageSource.getMessage(CREATE_FUND_BUTTON_TEXT, null, locale);

        final H3Header introductionHeader = new H3Header(messageSource, locale, NO_FUND_DETECTED_HEADER_TEXT);
        final Button createFundButton = new Button(createFundButtonText, event -> new FundRegistrationDialog(messageSource, locale, fundService).open());

        final HorizontalLayout buttonLayout = new HorizontalLayout(createFundButton);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        final Div introductionDiv = new Div(introductionHeader, buttonLayout);
        introductionDiv.setWidth("100%");
        introductionDiv.getStyle().set("text-align", "center");

        final VerticalLayout verticalLayout = new VerticalLayout(introductionDiv);
        verticalLayout.setSizeFull();
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        return verticalLayout;
    }

    private ComponentEventListener<ClickEvent<Button>> updateFund() {
        return event -> {
            final String fundSuccessfullyUpdated = messageSource.getMessage(SUCCESSFULLY_FUND_UPDATED, null, locale);
            if (
                    updateFundRequestBinder.writeBeanIfValid(updateFundRequest)
                            && addressDtoBinder.writeBeanIfValid(address)
            ) {
                updateFundRequest.setAddress(address);
                fundService.updateFund(updateFundRequest);
                NotificationFactory.success(fundSuccessfullyUpdated).open();
                UI.getCurrent().getPage().reload();
            }
        };
    }

    private ComponentEventListener<ClickEvent<Button>> addRequisiteButtonClickListener(final Long fundId) {
        return event -> {
            final Dialog addRequisiteDialog = new Dialog();
            addRequisiteDialog.open();
            final Binder<FundRequisiteDto> fundRequisiteBinder = new Binder<>();
            final FundRequisiteDto newRequisite = new FundRequisiteDto();
            final FundRequisiteBlockFormLayout requisiteBlockFormLayout = new FundRequisiteBlockFormLayout(messageSource, locale, fundRequisiteBinder);
            this.add(requisiteBlockFormLayout);


            final SaveButton saveRequisiteBlockButton = new SaveButton(messageSource, locale, deleteEvent -> {
                if (fundRequisiteBinder.writeBeanIfValid(newRequisite)) {
                    fundService.addOrUpdateRequisite(fundId, newRequisite);
                    addRequisiteDialog.close();
                    UI.getCurrent().getPage().reload();
                }
            });
            addRequisiteDialog.add(requisiteBlockFormLayout);
            addRequisiteDialog.getFooter().add(
                    saveRequisiteBlockButton,
                    new CancelButton(messageSource, locale, buttonClickEvent -> addRequisiteDialog.close()));
        };
    }
}
