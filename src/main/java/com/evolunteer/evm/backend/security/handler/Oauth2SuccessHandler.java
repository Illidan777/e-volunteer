package com.evolunteer.evm.backend.security.handler;

import com.evolunteer.evm.backend.security.principal.AuthenticationPrincipal;
import com.evolunteer.evm.backend.security.utils.SecurityUtils;
import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.domain.enums.user_management.AccountAuthType;
import com.evolunteer.evm.common.domain.request.CreateExternalUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String NAME_ATTRIBUTE = "given_name";
    private static final String SURNAME_ATTRIBUTE = "family_name";
    private static final String EMAIL_ATTRIBUTE = "email";

    private final AccountService accountService;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {
        final OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        final OAuth2User user = token.getPrincipal();

        final Map<String, Object> attributes = user.getAttributes();

        final Object nameAttribute = attributes.get(NAME_ATTRIBUTE);
        final Object surnameAttribute = attributes.get(SURNAME_ATTRIBUTE);
        final Object emailAttribute = attributes.get(EMAIL_ATTRIBUTE);

        if(Objects.isNull(nameAttribute) || Objects.isNull(surnameAttribute) || Objects.isNull(emailAttribute)) {
            log.warn("[Oauth2SuccessHandler] Invalid oauth attributes!");
            response.sendRedirect(SecurityUtils.Route.ACCOUNT_LOGIN_FAILURE_ROUTE);
            return;
        }

        final String email = emailAttribute.toString();

        final Optional<AccountDto> optionalAccountDto = accountService.getAccountByUsername(email);
        final AuthenticationPrincipal account;
        if (optionalAccountDto.isPresent()) {
            account = new AuthenticationPrincipal(optionalAccountDto.get());
        } else {
            final CreateExternalUserRequest createExternalUserRequest = CreateExternalUserRequest.builder()
                    .name(nameAttribute.toString())
                    .surname(surnameAttribute.toString())
                    .email(email)
                    .username(email)
                    .authType(AccountAuthType.GOOGLE_AUTH)
                    .build();
            final UserDto userDto = userService.registerExternalUser(createExternalUserRequest);
            account = new AuthenticationPrincipal(userDto.getAccountDetails());
        }
        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(account, authentication.getCredentials(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(result);
        response.sendRedirect("/");
    }
}
