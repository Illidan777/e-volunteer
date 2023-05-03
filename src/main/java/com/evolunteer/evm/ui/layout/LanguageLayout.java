package com.evolunteer.evm.ui.layout;

import com.evolunteer.evm.common.utils.LocalizationUtils;
import com.evolunteer.evm.ui.utils.CookieUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LanguageLayout extends VerticalLayout {

    public LanguageLayout() {
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.EVENLY);

        final Image ukrainianFlag = new Image("ukr-flag.jpg", "ukr-lang");
        final Image ukFlag = new Image("uk-flag.jpg", "english-lang");

        final Button ukrainianLocaleButton = new Button(ukrainianFlag, this.switchLanguageListener(LocalizationUtils.UKRAINIAN_LANGUAGE));
        final Button englishLocaleButton = new Button(ukFlag, this.switchLanguageListener(LocalizationUtils.ENGLISH_LANGUAGE));

        add(englishLocaleButton, ukrainianLocaleButton);
    }

    private ComponentEventListener<ClickEvent<Button>> switchLanguageListener(final String locale) {
        return buttonClickEvent -> {
            CookieUtils.addCookie(CookieUtils.LANGUAGE_COOKIE_NAME, locale);
            UI.getCurrent().getPage().reload();
        };
    }
}
