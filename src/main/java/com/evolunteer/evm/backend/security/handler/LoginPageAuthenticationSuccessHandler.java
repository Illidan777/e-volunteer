package com.evolunteer.evm.backend.security.handler;

import com.evolunteer.evm.backend.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginPageAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("[Oauth2SuccessHandler] Success handling oauth authentication.");
        response.sendRedirect(SecurityUtils.Route.HOME_REDIRECT_ROUTE);
    }
}
