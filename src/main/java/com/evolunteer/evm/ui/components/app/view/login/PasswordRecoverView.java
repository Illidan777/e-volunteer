package com.evolunteer.evm.ui.components.app.view.login;

import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.VerificationLinkService;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.enums.user_management.LinkVerificationResult;
import com.evolunteer.evm.common.domain.enums.user_management.VerificationLinkType;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.binder.bean.PasswordRecoveringBean;
import com.evolunteer.evm.ui.components.app.div.PasswordRecoverDiv;
import com.evolunteer.evm.ui.components.app.layout.VerificationLinkLayout;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.*;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_INVALID_LINK_ERROR;

@Route(RouteUtils.PASSWORD_RECOVER_ROUTE)
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
            this.add(new VerificationLinkLayout(messageSource, locale, VALIDATION_INVALID_LINK_ERROR));
            return;
        }
        final String accountId = queryParameters.get(ACCOUNT_ID_QUERY_PARAMETER_NAME).get(0);
        final String token = queryParameters.get(VERIFICATION_TOKEN_QUERY_PARAMETER_NAME).get(0);
        final String linkId = queryParameters.get(LINK_ID_QUERY_PARAMETER_NAME).get(0);
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(token) || StringUtils.isBlank(linkId)) {
            this.add(new VerificationLinkLayout(messageSource, locale, VALIDATION_INVALID_LINK_ERROR));
            return;
        }
        final long decodedAccountId;
        final long decodedLinkId;
        try {
            decodedAccountId = Long.parseLong(new String(Base64.getDecoder().decode(accountId)));
            decodedLinkId = Long.parseLong(new String(Base64.getDecoder().decode(linkId)));
        } catch (IllegalArgumentException e) {
            this.add(new VerificationLinkLayout(messageSource, locale, VALIDATION_INVALID_LINK_ERROR));
            return;
        }
        final LinkVerificationResult linkVerificationResult = verificationLinkService.verifyLink(decodedLinkId, token);
        if (!linkVerificationResult.isSuccess()) {
            this.add(new VerificationLinkLayout(accountService, messageSource, locale,
                    linkVerificationResult.getLocalizedMessage(), VerificationLinkType.PASSWORD_RECOVER, decodedAccountId));
            return;
        }
        final Optional<AccountDto> optionalAccountDto = accountService.getAccountById(decodedAccountId);
        if (optionalAccountDto.isEmpty()) {
            this.add(new VerificationLinkLayout(messageSource, locale, VALIDATION_INVALID_LINK_ERROR));
        } else {
            setHeightFull();
            setWidthFull();
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            this.add(
                    new PasswordRecoverDiv(messageSource, locale, accountService, decodedAccountId, true)
            );
        }
    }
}
