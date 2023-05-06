package com.evolunteer.evm.ui.view;

import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.VerificationLinkService;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.enums.user_management.LinkVerificationResult;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.binder.bean.PasswordRecoveringBean;
import com.evolunteer.evm.ui.button.ConfirmButton;
import com.evolunteer.evm.ui.notification.NotificationFactory;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.*;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_INVALID_LINK_ERROR;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_PASSWORD_CONFIRMING_ERROR;

@Route(RouteUtils.PASSWORD_RECOVER)
public class PasswordRecoverView extends VerticalLayout implements BeforeEnterObserver {

    private static final String ACCOUNT_ID_QUERY_PARAMETER_NAME = "accountId";
    private static final String VERIFICATION_TOKEN_QUERY_PARAMETER_NAME = "verificationToken";
    private static final String LINK_ID_QUERY_PARAMETER_NAME = "linkId";
    private final MessageSource messageSource;
    private final Locale locale;
    private final VerificationLinkService verificationLinkService;
    private final AccountService accountService;
    private final Binder<PasswordRecoveringBean> passwordRecoveringBinder;
    private final PasswordRecoveringBean passwordRecoveringBean;

    public PasswordRecoverView(MessageSource messageSource, AccountService accountService, VerificationLinkService verificationLinkService) {
        this.messageSource = messageSource;
        this.accountService = accountService;
        this.verificationLinkService = verificationLinkService;
        this.locale = LocalizationUtils.getLocale();
        this.passwordRecoveringBinder = new Binder<>();
        this.passwordRecoveringBean = new PasswordRecoveringBean();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        final Map<String, List<String>> queryParameters = beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        if (!queryParameters.containsKey(ACCOUNT_ID_QUERY_PARAMETER_NAME)
                || !queryParameters.containsKey(VERIFICATION_TOKEN_QUERY_PARAMETER_NAME)
                || !queryParameters.containsKey(LINK_ID_QUERY_PARAMETER_NAME)) {
            this.addInvalidLinkInfo(VALIDATION_INVALID_LINK_ERROR);
            return;
        }
        final String accountId = queryParameters.get(ACCOUNT_ID_QUERY_PARAMETER_NAME).get(0);
        final String token = queryParameters.get(VERIFICATION_TOKEN_QUERY_PARAMETER_NAME).get(0);
        final String linkId = queryParameters.get(LINK_ID_QUERY_PARAMETER_NAME).get(0);
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(token) || StringUtils.isBlank(linkId)) {
            this.addInvalidLinkInfo(VALIDATION_INVALID_LINK_ERROR);
            return;
        }
        final long decodedAccountId;
        final long decodedLinkId;
        try {
            decodedAccountId = Long.parseLong(new String(Base64.getDecoder().decode(accountId)));
            decodedLinkId = Long.parseLong(new String(Base64.getDecoder().decode(linkId)));
        } catch (IllegalArgumentException e) {
            this.addInvalidLinkInfo(VALIDATION_INVALID_LINK_ERROR);
            return;
        }
        final LinkVerificationResult linkVerificationResult = verificationLinkService.verifyLink(decodedLinkId, token);
        if(!linkVerificationResult.isSuccess()) {
            this.addInvalidLinkInfo(linkVerificationResult.getLocalizedMessage());
            return;
        }
        final Optional<AccountDto> optionalAccountDto = accountService.getAccountById(decodedAccountId);
        if (optionalAccountDto.isEmpty()) {
            this.addInvalidLinkInfo(VALIDATION_INVALID_LINK_ERROR);
        } else {
            this.addPasswordRecoveringComponents(decodedAccountId);
        }
    }

    private void addPasswordRecoveringComponents(final Long accountId) {
        setHeightFull();
        setWidthFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        final String headerText = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverView.HEADER_TEXT, null, locale);
        final String newPasswordField = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverView.NEW_PASSWORD_FIELD_TEXT, null, locale);
        final String newPasswordConfirmingField = messageSource.getMessage(LocalizationUtils.UI.PasswordRecoverView.NEW_PASSWORD_CONFIRMING_FIELD_TEXT, null, locale);
        final String passwordValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_PASSWORD_ERROR, null, locale);

        final H3 passwordRecoveringHeader = new H3(headerText);

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

        final ConfirmButton confirmButton = new ConfirmButton(messageSource, locale, this.recoverPassword(accountId));

        final FormLayout passwordRecoveringForm = new FormLayout();
        passwordRecoveringForm.getStyle().set("margin", "auto");
        passwordRecoveringForm.setWidth("70%");

        passwordRecoveringForm.add(passwordRecoveringHeader, newPassword, newPasswordConfirming, confirmButton);
        passwordRecoveringForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));

        add(passwordRecoveringForm);
    }

    private ComponentEventListener<ClickEvent<Button>> recoverPassword(final Long accountId) {
        return buttonClickEvent -> {
            if (passwordRecoveringBinder.writeBeanIfValid(passwordRecoveringBean)) {
                final String passwordsDoNotMatch = messageSource.getMessage(VALIDATION_PASSWORD_CONFIRMING_ERROR, null, locale);

                final String newPassword = passwordRecoveringBean.getPassword();
                final String confirmingPassword = passwordRecoveringBean.getPasswordConfirming();
                if (!newPassword.equals(confirmingPassword)) {
                    NotificationFactory.error(passwordsDoNotMatch).open();
                    return;
                }
                accountService.recoverPasswordById(accountId, newPassword);
                UI.getCurrent().getPage().setLocation(RouteUtils.LOGIN_ROUTE);

            }
        };
    }

    private void addInvalidLinkInfo(final String message) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        final Div invalidLinkInfoDiv = new Div();
        invalidLinkInfoDiv.setWidth("100%");
        invalidLinkInfoDiv.getStyle().set("text-align", "center");
        final Button goBackToLogin = new Button(
                messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.GO_BACK_TO_LOGIN_BUTTON_TEXT, null, locale),
                event -> UI.getCurrent().navigate(RouteUtils.LOGIN_ROUTE)
        );
        goBackToLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        invalidLinkInfoDiv.setText(messageSource.getMessage(message, null, locale));

        final HorizontalLayout buttonLayout = new HorizontalLayout(goBackToLogin);
        buttonLayout.setWidth("100%");
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        invalidLinkInfoDiv.add(buttonLayout);
        this.add(invalidLinkInfoDiv);
    }
}
