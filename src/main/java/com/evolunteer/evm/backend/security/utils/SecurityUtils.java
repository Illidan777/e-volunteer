package com.evolunteer.evm.backend.security.utils;

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

public class SecurityUtils {

    private SecurityUtils() {
        // Util methods only
    }

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

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Route {
        public static final String ACCOUNT_LOGIN_ROUTE = "/login";
        public static final String OAUTH_GOOGLE_LOGIN_ROUTE = "/oauth2/authorization/google";
        public static final String INTRODUCTION_ROUTE = "/introduction";

        public static String[] allowedRoutes() {
            return listOfAllowedRoutes().toArray(new String[0]);
        }

        private static List<String> listOfAllowedRoutes() {
            return List.of(ACCOUNT_LOGIN_ROUTE, INTRODUCTION_ROUTE);
        }
    }
}
