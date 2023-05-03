package com.evolunteer.evm.ui.utils;

import com.vaadin.flow.server.VaadinService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtils {
    public static final String LANGUAGE_COOKIE_NAME = "lang";

    public static void addCookie(final String name, final String value) {
        final Cookie cookie = new Cookie(name, value);
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    public static Optional<Cookie> getCookieValue(final String name) {
        return Arrays.stream(VaadinService.getCurrentRequest().getCookies())
                .filter(c -> name.equals(c.getName()))
                .findFirst();
    }
}
