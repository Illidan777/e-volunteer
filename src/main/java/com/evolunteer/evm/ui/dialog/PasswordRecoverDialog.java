package com.evolunteer.evm.ui.dialog;

import com.evolunteer.evm.backend.service.notification_management.sender.NotificationService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.notification_management.NotificationRequest;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.enums.notification_management.NotificationProviderType;
import com.evolunteer.evm.common.domain.request.CreateAccountRequest;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.button.CancelButton;
import com.evolunteer.evm.ui.button.ConfirmButton;
import com.evolunteer.evm.ui.notification.NotificationFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Base64;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.EmailNotification.*;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_ACCOUNT_DOES_NOT_EXIST_BY_USERNAME_ERROR;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_ACCOUNT_IS_NOT_VERIFIED_ERROR;

public class PasswordRecoverDialog extends Dialog {

    private final Locale locale;
    private final MessageSource messageSource;
    private final ConfirmButton confirmButton;
    private final UserService userService;
    private final NotificationService notificationService;
    private final Binder<String> usernameBinder;
    private final TextField usernameField;
    private final String passwordRecoverLinkPrefix;

    public PasswordRecoverDialog(MessageSource messageSource,
                                 Locale locale,
                                 UserService userService,
                                 NotificationService notificationService,
                                 String passwordRecoverLinkPrefix) {
        this.locale = locale;
        this.messageSource = messageSource;
        this.userService = userService;
        this.notificationService = notificationService;
        this.usernameBinder = new Binder<>();
        this.passwordRecoverLinkPrefix = passwordRecoverLinkPrefix;

        final String headerText = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverDialog.HEADER_TEXT, null, locale);
        final String usernameFieldText = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverDialog.USERNAME_FIELD_TEXT, null, locale);
        final String usernameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_USERNAME_ERROR, null, locale);

        final H3 passwordRecoveringHeader = new H3(headerText);

        usernameField = new TextField(usernameFieldText);
        usernameField.setRequired(true);
        usernameField.setRequiredIndicatorVisible(true);
        usernameBinder.forField(usernameField)
                .withValidator(value -> !StringUtils.isBlank(value), usernameValidationText)
                .bind(value -> usernameField.getValue(), (name, value) -> usernameField.setValue(value));

        confirmButton = new ConfirmButton(messageSource, locale, this.sendPasswordRecoveringConfirmationLink());

        final FormLayout passwordRecoveringForm = new FormLayout();

        passwordRecoveringForm.add(passwordRecoveringHeader, usernameField);
        passwordRecoveringForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));

        add(passwordRecoveringForm);
        this.getFooter().add(confirmButton, new CancelButton(messageSource, locale, buttonClickEvent -> this.close()));
    }

    private ComponentEventListener<ClickEvent<Button>> sendPasswordRecoveringConfirmationLink() {
        return buttonClickEvent -> {
            if (usernameBinder.validate().isOk()) {
                final String username = usernameField.getValue();
                final String emailConfirmationText = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverDialog.EMAIL_CONFIRMATION_TEXT, null, locale);
                final String accountDoesNotExistValidationText = messageSource.getMessage(VALIDATION_ACCOUNT_DOES_NOT_EXIST_BY_USERNAME_ERROR,
                        new String[]{username}, locale);
                final String accountIsNotVerifiedText = messageSource.getMessage(VALIDATION_ACCOUNT_IS_NOT_VERIFIED_ERROR, null, locale);

                final Optional<UserDto> optionalUserDto = userService.getUserByUsername(username);
                if (optionalUserDto.isPresent()) {
                    final UserDto user = optionalUserDto.get();
                    final AccountDto account = user.getAccountDetails();
                    final String encodedAccountId = Base64.getEncoder().encodeToString(account.getId().toString().getBytes());
                    if (account.getStatus().isExpired()) {
                        NotificationFactory.error(accountDoesNotExistValidationText).open();
                        return;
                    }
                    if (account.getStatus().isNotVerified()) {
                        NotificationFactory.error(accountIsNotVerifiedText).open();
                        return;
                    }
                    this.removeAll();
                    this.sendPasswordRecoverNotification(encodedAccountId, user.getEmail());

                    final VerticalLayout verticalLayout = new VerticalLayout();
                    verticalLayout.setSizeFull();
                    verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                    verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

                    final Icon icon = new Icon(VaadinIcon.ENVELOPE_O);
                    final Span message = new Span(emailConfirmationText);
                    verticalLayout.add(message, icon);
                    add(verticalLayout);
                    this.getFooter().remove(confirmButton);

                } else {
                    NotificationFactory.error(accountDoesNotExistValidationText).open();
                }
            }
        };
    }

    private void sendPasswordRecoverNotification(final String encodedAccountId, final String userEmail) {
        final String passwordRecoveringLink = String.format(passwordRecoverLinkPrefix, encodedAccountId);
        final String verificationAccountSubject = messageSource.getMessage(ACCOUNT_PASSWORD_RECOVER_NOTIFICATION_SUBJECT,
                null, locale);
        final String verificationAccountPattern = messageSource.getMessage(ACCOUNT_PASSWORD_RECOVER_NOTIFICATION_PATTERN,
                null, locale);
        final NotificationRequest notificationRequest = NotificationRequest.of(
                String.format(verificationAccountPattern, passwordRecoveringLink),
                verificationAccountSubject,
                NotificationProviderType.EMAIL,
                Set.of(userEmail)
        );
        notificationService.send(notificationRequest);
    }
}
