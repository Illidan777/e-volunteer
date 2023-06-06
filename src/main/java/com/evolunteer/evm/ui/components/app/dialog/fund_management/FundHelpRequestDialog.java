package com.evolunteer.evm.ui.components.app.dialog.fund_management;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.dto.fund_management.FundHelpRequestDto;
import com.evolunteer.evm.common.domain.dto.fund_management.HelpRequestExecutorDto;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.string.StringSymbolUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.components.general.button.CancelButton;
import com.evolunteer.evm.ui.components.general.button.SendButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

public class FundHelpRequestDialog extends Dialog {

    private final MessageSource messageSource;
    private final Locale locale;
    private final Binder<FundHelpRequestDto> fundHelpRequestDtoBinder;
    private final FundHelpRequestDto helpRequestDto;
    private final Binder<HelpRequestExecutorDto> helpRequestExecutorDtoBinder;
    private final HelpRequestExecutorDto executorDto;
    private final FundService fundService;
    private final FundDtoFull fund;

    public FundHelpRequestDialog(MessageSource messageSource, Locale locale, FundService fundService, FundDtoFull fund) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.fundHelpRequestDtoBinder = new Binder<>();
        this.helpRequestExecutorDtoBinder = new Binder<>();
        this.fundService = fundService;
        this.fund = fund;
        this.helpRequestDto = new FundHelpRequestDto();
        this.executorDto = new HelpRequestExecutorDto();

        final String descriptionFieldText = messageSource.getMessage(LocalizationUtils.UI.VisitorView.DESCRIPTION_FILED_TEXT, null, locale);
        final String organizationName = messageSource.getMessage(LocalizationUtils.UI.VisitorView.ORGANIZATION_NAME_FILED_TEXT, null, locale);
        final String nameFieldText = messageSource.getMessage(LocalizationUtils.UI.UserRegistrationDialog.NAME_FIELD_TEXT, null, locale);
        final String surnameFieldText = messageSource.getMessage(LocalizationUtils.UI.UserRegistrationDialog.SURNAME_FIELD_TEXT, null, locale);
        final String middleNameFieldText = messageSource.getMessage(LocalizationUtils.UI.UserRegistrationDialog.MIDDLE_NAME_FIELD_TEXT, null, locale);
        final String phoneFieldText = messageSource.getMessage(LocalizationUtils.UI.UserRegistrationDialog.PHONE_FIELD_TEXT, null, locale);
        final String emailFieldText = messageSource.getMessage(LocalizationUtils.UI.UserRegistrationDialog.EMAIL_FIELD_TEXT, null, locale);

        final String nameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_USER_NAME_ERROR, null, locale);
        final String surnameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_SURNAME_ERROR, null, locale);
        final String phoneValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_PHONE_ERROR, null, locale);
        final String emailValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_EMAIL_ERROR, null, locale);
        final String descriptionValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_HELP_REQUEST_DESCRIPTION_ERROR, null, locale);

        final H3Header contactInformationHeader = new H3Header(messageSource, locale, LocalizationUtils.UI.VisitorView.CONTACT_INFO_HEADER);
        final H3Header detailsHeader = new H3Header(messageSource, locale, LocalizationUtils.UI.VisitorView.DETAILS_HEADER_TEXT);

        final TextField nameField = new TextField(nameFieldText);
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);
        helpRequestExecutorDtoBinder.forField(nameField)
                .withValidator(name -> !StringUtils.isBlank(name), nameValidationText)
                .bind(HelpRequestExecutorDto::getName, HelpRequestExecutorDto::setName);

        final TextField surnameField = new TextField(surnameFieldText);
        surnameField.setRequired(true);
        surnameField.setRequiredIndicatorVisible(true);
        helpRequestExecutorDtoBinder.forField(surnameField)
                .withValidator(surname -> !StringUtils.isBlank(surname), surnameValidationText)
                .bind(HelpRequestExecutorDto::getSurname, HelpRequestExecutorDto::setSurname);

        final TextField middleNameField = new TextField(middleNameFieldText);
        helpRequestExecutorDtoBinder.forField(middleNameField)
                .bind(HelpRequestExecutorDto::getMiddleName, HelpRequestExecutorDto::setMiddleName);

        final TextField phoneField = new TextField(phoneFieldText);
        phoneField.setRequired(true);
        phoneField.setRequiredIndicatorVisible(true);
        helpRequestExecutorDtoBinder.forField(phoneField)
                .withValidator(new RegexpValidator(phoneValidationText, ValidationUtils.NUMBER_REGEX))
                .bind(HelpRequestExecutorDto::getPhone, HelpRequestExecutorDto::setPhone);

        final EmailField emailField = new EmailField(emailFieldText);
        emailField.setRequiredIndicatorVisible(true);
        helpRequestExecutorDtoBinder.forField(emailField)
                .withValidator(new RegexpValidator(emailValidationText, ValidationUtils.EMAIL_REGEX))
                .bind(HelpRequestExecutorDto::getEmail, HelpRequestExecutorDto::setEmail);

        final TextField organizationNameField = new TextField(organizationName);
        helpRequestExecutorDtoBinder.forField(organizationNameField)
                .bind(HelpRequestExecutorDto::getOrganizationName, HelpRequestExecutorDto::setOrganizationName);

        final TextArea detailsField = new TextArea(descriptionFieldText);
        detailsField.setRequired(true);
        detailsField.setRequiredIndicatorVisible(true);
        fundHelpRequestDtoBinder.forField(detailsField)
                .withValidator(details -> !StringUtils.isBlank(details), descriptionValidationText)
                .bind(FundHelpRequestDto::getDescription, FundHelpRequestDto::setDescription);

        final SendButton confirmButton = new SendButton(messageSource, locale, this.createHelpRequest());

        fundHelpRequestDtoBinder.setBean(helpRequestDto);
        helpRequestExecutorDtoBinder.setBean(executorDto);

        final FormLayout requestForHelpFormLayout = new FormLayout();

        requestForHelpFormLayout.add(contactInformationHeader, organizationNameField, nameField, surnameField, middleNameField,
                phoneField, emailField, detailsHeader, detailsField);
        requestForHelpFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));

        this.getFooter().add(confirmButton, new CancelButton(messageSource, locale, buttonClickEvent -> this.close()));
        this.add(requestForHelpFormLayout);
    }

    private ComponentEventListener<ClickEvent<Button>> createHelpRequest() {
        return event -> {

            if (helpRequestExecutorDtoBinder.writeBeanIfValid(executorDto) && fundHelpRequestDtoBinder.writeBeanIfValid(helpRequestDto)) {
                final String requestSuccessfullySent = messageSource.getMessage(LocalizationUtils.UI.VisitorView.HELP_REQUEST_SUCCESSFULLY_SEND_TEXT, null, locale);

                final LocalDateTime currentTime = LocalDateTime.now();
                final String requestNumber = currentTime.toString().replaceAll("\\D+", StringSymbolUtils.EMPTY_STRING);

                helpRequestDto.setExecutor(executorDto);
                helpRequestDto.setNumber(requestNumber);
                fundService.createFundHelpRequest(fund.getId(), helpRequestDto);
                this.removeAll();
                this.getFooter().removeAll();

                final VerticalLayout successfullyRequestProceedInfo = new VerticalLayout();
                successfullyRequestProceedInfo.setSizeFull();
                successfullyRequestProceedInfo.setAlignItems(FlexComponent.Alignment.CENTER);
                successfullyRequestProceedInfo.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
                successfullyRequestProceedInfo.add(
                        new Span(requestSuccessfullySent),
                        new H3Header(requestNumber)
                );
                this.add(successfullyRequestProceedInfo);
            }
        };
    }
}
