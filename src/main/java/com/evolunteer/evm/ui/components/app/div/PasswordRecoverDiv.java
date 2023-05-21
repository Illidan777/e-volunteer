package com.evolunteer.evm.ui.components.app.div;

import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.components.app.binder.bean.PasswordRecoveringBean;
import com.evolunteer.evm.ui.components.general.button.SaveButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_PASSWORD_CONFIRMING_ERROR;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.PasswordRecoverView.PASSWORD_SUCCESSFULLY_UPDATED;

public class PasswordRecoverDiv extends Div {

    private final AccountService accountService;
    private final MessageSource messageSource;
    private final Locale locale;
    private final Binder<PasswordRecoveringBean> passwordRecoveringBinder;
    private final PasswordRecoveringBean passwordRecoveringBean;

    public PasswordRecoverDiv(MessageSource messageSource, Locale locale, AccountService accountService, Long accountId, boolean navigateToLogin) {
        this.messageSource = messageSource;
        this.accountService = accountService;
        this.locale = locale;
        this.passwordRecoveringBinder = new Binder<>();
        this.passwordRecoveringBean = new PasswordRecoveringBean();

        final String newPasswordField = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverView.NEW_PASSWORD_FIELD_TEXT, null, locale);
        final String newPasswordConfirmingField = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverView.NEW_PASSWORD_CONFIRMING_FIELD_TEXT, null, locale);
        final String passwordValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_PASSWORD_ERROR, null, locale);

        final H3Header passwordRecoveringHeader = new H3Header(messageSource, locale, LocalizationUtils.UI.PasswordRecoverView.HEADER_TEXT);

        final PasswordField newPassword = new PasswordField(newPasswordField);
        newPassword.setRequired(true);
        newPassword.setRequiredIndicatorVisible(true);
        passwordRecoveringBinder.forField(newPassword)
                .withValidator(new RegexpValidator(passwordValidationText, ValidationUtils.PASSWORD_REGEX))
                .bind(PasswordRecoveringBean::getPassword, PasswordRecoveringBean::setPassword);

        final PasswordField newPasswordConfirming = new PasswordField(newPasswordConfirmingField);
        newPasswordConfirming.setRequired(true);
        newPasswordConfirming.setRequiredIndicatorVisible(true);
        passwordRecoveringBinder.forField(newPasswordConfirming)
                .withValidator(new RegexpValidator(passwordValidationText, ValidationUtils.PASSWORD_REGEX))
                .bind(PasswordRecoveringBean::getPasswordConfirming, PasswordRecoveringBean::setPasswordConfirming);

        passwordRecoveringBinder.setBean(passwordRecoveringBean);

        final SaveButton saveButton = new SaveButton(messageSource, locale, this.recoverPassword(accountId, navigateToLogin));
        final HorizontalLayout buttonLayout = new HorizontalLayout(saveButton);
        buttonLayout.setAlignItems(FlexComponent.Alignment.START);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        final FormLayout passwordRecoveringForm = new FormLayout();

        passwordRecoveringForm.add(newPassword, newPasswordConfirming, buttonLayout);
        passwordRecoveringForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));

        add(passwordRecoveringHeader, passwordRecoveringForm);
    }

    private ComponentEventListener<ClickEvent<Button>> recoverPassword(final Long accountId, boolean navigateToLogin) {
        return buttonClickEvent -> {
            if (passwordRecoveringBinder.writeBeanIfValid(passwordRecoveringBean)) {
                final String passwordsDoNotMatch = messageSource.getMessage(VALIDATION_PASSWORD_CONFIRMING_ERROR, null, locale);
                final String passwordSuccessfullyUpdatedMessage = messageSource.getMessage(PASSWORD_SUCCESSFULLY_UPDATED, null, locale);

                final String newPassword = passwordRecoveringBean.getPassword();
                final String confirmingPassword = passwordRecoveringBean.getPasswordConfirming();
                if (!newPassword.equals(confirmingPassword)) {
                    NotificationFactory.error(passwordsDoNotMatch).open();
                    return;
                }
                accountService.recoverPasswordById(accountId, newPassword);
                NotificationFactory.success(passwordSuccessfullyUpdatedMessage).open();
                if(navigateToLogin) {
                    UI.getCurrent().getPage().setLocation(RouteUtils.LOGIN_ROUTE);
                }
            }
        };
    }
}
