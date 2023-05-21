package com.evolunteer.evm.backend.security.utils;

import com.evolunteer.evm.backend.security.principal.AuthenticationPrincipal;
import com.evolunteer.evm.common.domain.dto.user_management.AccountDto;
import com.evolunteer.evm.common.domain.exception.auth.AuthenticationException;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(HandlerHelper.RequestType.values())
                .anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    public static boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated();
    }

    public static AccountDto getContextAccount() {
        final Authentication contextAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (!contextAuthentication.isAuthenticated()) {
            throw new AuthenticationException(format("Context account has not authenticated. Username: %s",
                    contextAuthentication.getName()));
        } else {
            return ((AuthenticationPrincipal) contextAuthentication.getPrincipal()).getAccount();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Route {
        public static final String ACCOUNT_LOGIN_FAILURE_ROUTE = "/login?error";
        public static final String ACCOUNT_LOGIN_ROUTE = "/login";
        public static final String ACCOUNT_LOGOUT_ROUTE = "/logout";
        public static final String OAUTH_GOOGLE_LOGIN_ROUTE = "/oauth2/authorization/google";
        public static final String INTRODUCTION_ROUTE = "/introduction";
        public static final String ACCOUNT_VERIFICATION_ROUTE = "/verification/**";
        public static final String PASSWORD_RECOVER_ROUTE = "/password-recover/**";
        public static final String HOME_REDIRECT_ROUTE = "/home";

        public static String[] allowedRoutes() {
            return listOfAllowedRoutes().toArray(new String[0]);
        }

        private static List<String> listOfAllowedRoutes() {
            return List.of(
                    ACCOUNT_LOGIN_ROUTE,
                    INTRODUCTION_ROUTE,
                    ACCOUNT_VERIFICATION_ROUTE,
                    PASSWORD_RECOVER_ROUTE
                    );
        }
    }
}
