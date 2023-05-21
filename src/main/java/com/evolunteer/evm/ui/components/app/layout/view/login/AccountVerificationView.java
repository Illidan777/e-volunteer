package com.evolunteer.evm.ui.components.app.layout.view.login;

import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.common.domain.enums.user_management.LinkVerificationResult;
import com.evolunteer.evm.common.domain.enums.user_management.VerificationLinkType;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.layout.VerificationLinkLayout;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_INVALID_LINK_ERROR;

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
        final Map<String, List<String>> queryParameters = beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        if (!queryParameters.containsKey(ACCOUNT_ID_QUERY_PARAMETER_NAME) || !queryParameters.containsKey(VERIFICATION_TOKEN_QUERY_PARAMETER_NAME)) {
            this.add(new VerificationLinkLayout(messageSource, locale, VALIDATION_INVALID_LINK_ERROR));
            return;
        }
        final String accountId = queryParameters.get(ACCOUNT_ID_QUERY_PARAMETER_NAME).get(0);
        final String token = queryParameters.get(VERIFICATION_TOKEN_QUERY_PARAMETER_NAME).get(0);
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(token)) {
            this.add(new VerificationLinkLayout(messageSource, locale, VALIDATION_INVALID_LINK_ERROR));
            return;
        }
        final long decodedAccountId;
        try {
            decodedAccountId = Long.parseLong(new String(Base64.getDecoder().decode(accountId)));
        } catch (IllegalArgumentException e) {
            this.add(new VerificationLinkLayout(messageSource, locale, VALIDATION_INVALID_LINK_ERROR));
            return;
        }
        final LinkVerificationResult linkVerificationResult = accountService.verifyAccount(accountId, token);
        if (linkVerificationResult.isSuccess()) {
            this.add(new VerificationLinkLayout(messageSource, locale, linkVerificationResult.getLocalizedMessage()));
        } else {
            this.add(new VerificationLinkLayout(accountService, messageSource, locale, linkVerificationResult.getLocalizedMessage(), VerificationLinkType.ACCOUNT_VERIFICATION, decodedAccountId));
        }
    }
}
