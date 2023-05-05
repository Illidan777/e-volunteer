package com.evolunteer.evm.ui.view;

import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.common.domain.enums.user_management.TokenVerificationResult;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.notification.NotificationFactory;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_VERIFICATION_LINK_ERROR;

@Route(RouteUtils.ACCOUNT_VERIFICATION)
public class AccountVerificationView extends VerticalLayout implements BeforeEnterObserver {

    private static final String ACCOUNT_ID_QUERY_PARAMETER_NAME = "accountId";
    private static final String VERIFICATION_TOKEN_QUERY_PARAMETER_NAME = "verificationToken";
    private final MessageSource messageSource;
    private final Locale locale;
    private final AccountService accountService;

    public AccountVerificationView(MessageSource messageSource, AccountService accountService) {
        this.messageSource = messageSource;
        this.accountService = accountService;
        this.locale = LocalizationUtils.getLocale();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        final Button goBackToLogin = new Button(
                messageSource.getMessage(LocalizationUtils.UI.RegistrationDialog.GO_BACK_TO_LOGIN_BUTTON_TEXT, null, locale),
                event -> UI.getCurrent().navigate(RouteUtils.LOGIN_ROUTE)
        );
        goBackToLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        final String verificationError;
        final Map<String, List<String>> queryParameters = beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        if(!queryParameters.containsKey(ACCOUNT_ID_QUERY_PARAMETER_NAME) || !queryParameters.containsKey(VERIFICATION_TOKEN_QUERY_PARAMETER_NAME)) {
            verificationError = messageSource.getMessage(VALIDATION_VERIFICATION_LINK_ERROR, null, locale);
            NotificationFactory.error(verificationError).open();
            this.add(goBackToLogin);
            return;
        }
        final String accountId = queryParameters.get(ACCOUNT_ID_QUERY_PARAMETER_NAME).get(0);
        final String token = queryParameters.get(VERIFICATION_TOKEN_QUERY_PARAMETER_NAME).get(0);
        if(StringUtils.isBlank(accountId) || StringUtils.isBlank(token)) {
            verificationError = messageSource.getMessage(VALIDATION_VERIFICATION_LINK_ERROR, null, locale);
            NotificationFactory.error(verificationError).open();
            this.add(goBackToLogin);
            return;
        }
        final TokenVerificationResult tokenVerificationResult = accountService.verifyAccount(accountId, token);
        if(tokenVerificationResult.getIsError()) {
            verificationError = messageSource.getMessage(tokenVerificationResult.getLocalizedMessage(), null, locale);
            NotificationFactory.error(verificationError).open();
        } else {
            verificationError = messageSource.getMessage(tokenVerificationResult.getLocalizedMessage(), null, locale);
            NotificationFactory.success(verificationError).open();
        }
        this.add(goBackToLogin);
    }
}
