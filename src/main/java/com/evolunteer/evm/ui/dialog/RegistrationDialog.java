package com.evolunteer.evm.ui.dialog;

import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.request.CreateUserRequest;
import com.evolunteer.evm.common.utils.date.DateUtils;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.button.CancelButton;
import com.evolunteer.evm.ui.button.ConfirmButton;
import com.evolunteer.evm.ui.notification.NotificationFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_ACCOUNT_ALREADY_EXIST_BY_USERNAME_ERROR;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.RegistrationDialog.SUCCESS_REGISTRATION_TEXT;

public class RegistrationDialog extends Dialog {

    private final ConfirmButton confirmButton;
    private final Binder<CreateUserRequest> createUserRequestBinder;
    private final CreateUserRequest createUserRequest;
    private final Locale locale;
    private final MessageSource messageSource;
    private final AccountService accountService;
    private final UserService userService;

    public RegistrationDialog(MessageSource messageSource, Locale locale, UserService userService, AccountService accountService) {
        this.locale = locale;
        this.messageSource = messageSource;
        this.userService = userService;
        this.accountService = accountService;
        this.createUserRequest = new CreateUserRequest();
        this.createUserRequestBinder = new Binder<>();

        final String headerText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.HEADER_TEXT, null, locale);
        final String credentialsHeaderText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.CREDENTIALS_HEADER_TEXT, null, locale);
        final String nameFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.NAME_FIELD_TEXT, null, locale);
        final String surnameFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.SURNAME_FIELD_TEXT, null, locale);
        final String middleNameFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.MIDDLE_NAME_FIELD_TEXT, null, locale);

        final String phoneFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.PHONE_FIELD_TEXT, null, locale);
        final String emailFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.EMAIL_FIELD_TEXT, null, locale);
        final String birthDateFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.BIRTHDATE_FIELD_TEXT, null, locale);
        final String usernameFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.USERNAME_FIELD_TEXT, null, locale);
        final String passwordFieldText = messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.PASSWORD_FIELD_TEXT, null, locale);

        final String nameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_USER_NAME_ERROR, null, locale);
        final String surnameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_SURNAME_ERROR, null, locale);
        final String phoneValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_PHONE_ERROR, null, locale);
        final String emailValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_EMAIL_ERROR, null, locale);
        final String birthDateValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_BIRTHDATE_ERROR, null, locale);
        final String usernameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_USERNAME_ERROR, null, locale);
        final String passwordValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_PASSWORD_ERROR, null, locale);

        final H3 registrationFormHeader = new H3(headerText);
        final H3 credentialsHeader = new H3(credentialsHeaderText);

        final TextField nameField = new TextField(nameFieldText);
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);
        createUserRequestBinder.forField(nameField)
                .withValidator(name -> !StringUtils.isBlank(name), nameValidationText)
                .bind(CreateUserRequest::getName, CreateUserRequest::setName);

        final TextField surnameField = new TextField(surnameFieldText);
        surnameField.setRequired(true);
        surnameField.setRequiredIndicatorVisible(true);
        createUserRequestBinder.forField(surnameField)
                .withValidator(surname -> !StringUtils.isBlank(surname), surnameValidationText)
                .bind(CreateUserRequest::getSurname, CreateUserRequest::setSurname);

        final TextField middleNameField = new TextField(middleNameFieldText);
        createUserRequestBinder.forField(middleNameField)
                .bind(CreateUserRequest::getMiddleName, CreateUserRequest::setMiddleName);

        final TextField phoneField = new TextField(phoneFieldText);
        phoneField.setRequired(true);
        phoneField.setRequiredIndicatorVisible(true);
        createUserRequestBinder.forField(phoneField)
                .withValidator(new RegexpValidator(phoneValidationText, ValidationUtils.NUMBER_REGEX))
                .bind(CreateUserRequest::getPhone, CreateUserRequest::setPhone);

        final EmailField emailField = new EmailField(emailFieldText);
        emailField.setRequiredIndicatorVisible(true);
        createUserRequestBinder.forField(emailField)
                .withValidator(new RegexpValidator(emailValidationText, ValidationUtils.EMAIL_REGEX))
                .bind(CreateUserRequest::getEmail, CreateUserRequest::setEmail);

        final DatePicker birthDateField = new DatePicker(birthDateFieldText);
        birthDateField.setRequired(true);
        birthDateField.setRequiredIndicatorVisible(true);
        createUserRequestBinder.forField(birthDateField)
                .withValidator(Objects::nonNull, birthDateValidationText)
                .bind(CreateUserRequest::getBirthDate, CreateUserRequest::setBirthDate);

        final DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
        singleFormatI18n.setDateFormat(DateUtils.DEFAULT_DATE_FORMAT);
        birthDateField.setI18n(singleFormatI18n);

        final TextField usernameField = new TextField(usernameFieldText);
        usernameField.setRequired(true);
        usernameField.setRequiredIndicatorVisible(true);
        createUserRequestBinder.forField(usernameField)
                .withValidator(username -> !StringUtils.isBlank(username), usernameValidationText)
                .bind(CreateUserRequest::getUsername, CreateUserRequest::setUsername);

        final PasswordField passwordField = new PasswordField(passwordFieldText);
        passwordField.setRequired(true);
        passwordField.setRequiredIndicatorVisible(true);
        createUserRequestBinder.forField(passwordField)
                .withValidator(new RegexpValidator(passwordValidationText, ValidationUtils.PASSWORD_REGEX))
                .bind(CreateUserRequest::getPassword, CreateUserRequest::setPassword);

        confirmButton = new ConfirmButton(messageSource, locale, this.registration());

        createUserRequestBinder.setBean(createUserRequest);

        final FormLayout registrationForm = new FormLayout();

        registrationForm.add(registrationFormHeader, nameField, surnameField, middleNameField,
                phoneField, emailField, birthDateField, credentialsHeader, usernameField, passwordField);
        registrationForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));

        add(registrationForm);
        this.getFooter().add(confirmButton, new CancelButton(messageSource, locale, buttonClickEvent -> this.close()));
    }

    private ComponentEventListener<ClickEvent<Button>> registration() {
        return buttonClickEvent -> {
            if (createUserRequestBinder.writeBeanIfValid(createUserRequest)) {
                final String successfullyRegistration = messageSource.getMessage(SUCCESS_REGISTRATION_TEXT, null, locale);
                final String accountAlreadyExistValidationText = messageSource.getMessage(VALIDATION_ACCOUNT_ALREADY_EXIST_BY_USERNAME_ERROR,
                        new String[]{createUserRequest.getUsername()}, locale);

                final Optional<AccountDto> optionalAccountDto = accountService.getAccountByUsername(createUserRequest.getUsername());
                if (optionalAccountDto.isPresent() && !optionalAccountDto.get().getStatus().isExpired()) {
                    NotificationFactory.error(accountAlreadyExistValidationText).open();
                } else  {
                    userService.registerUser(createUserRequest);
                    this.removeAll();
                    this.setWidth("500px");
                    this.setHeight("300px");


                    final VerticalLayout verticalLayout = new VerticalLayout();
                    verticalLayout.setSizeFull();
                    verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                    verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

                    final Icon icon = new Icon(VaadinIcon.ENVELOPE_O);
                    final Span message = new Span(successfullyRegistration);
                    verticalLayout.add(message, icon);
                    add(verticalLayout);
                    this.getFooter().remove(confirmButton);
                }
            }
        };
    }
}
