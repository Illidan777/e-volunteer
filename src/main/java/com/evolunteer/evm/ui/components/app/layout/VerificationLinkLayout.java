package com.evolunteer.evm.ui.components.app.layout;

import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.common.domain.enums.user_management.VerificationLinkType;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class VerificationLinkLayout extends VerticalLayout {

    private final MessageSource messageSource;
    private final Locale locale;

    public VerificationLinkLayout(AccountService accountService, MessageSource messageSource, Locale locale, String message, VerificationLinkType type, Long accountId) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.init();

        final Div verificationInfoDiv = this.createInfoDiv(message);
        final HorizontalLayout buttonLayout = this.createButtonLayout();
        final Button getNewLink = new Button(
                messageSource.getMessage(LocalizationUtils.UI.VerificationLinkLayout.GET_NEW_LINK_BUTTON_TEXT, null, locale),
                event -> {
                    accountService.sendVerificationLink(accountId, type);
                    UI.getCurrent().navigate(RouteUtils.LOGIN_ROUTE);
                });
        getNewLink.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(getNewLink);
        verificationInfoDiv.add(buttonLayout);
        this.add(verificationInfoDiv);
    }

    public VerificationLinkLayout(MessageSource messageSource, Locale locale, String message) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.init();

        final Div verificationInfoDiv = this.createInfoDiv(message);
        final HorizontalLayout buttonLayout = this.createButtonLayout();
        verificationInfoDiv.add(buttonLayout);
        this.add(verificationInfoDiv);
    }

    private Div createInfoDiv(final String message) {
        final Div verificationInfoDiv = new Div();
        verificationInfoDiv.setWidth("100%");
        verificationInfoDiv.getStyle().set("text-align", "center");
        verificationInfoDiv.setText(messageSource.getMessage(message, null, locale));
        return verificationInfoDiv;
    }

    private void init() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private HorizontalLayout createButtonLayout() {
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth("100%");
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        final Button goBackToLogin = new Button(
                messageSource.getMessage(LocalizationUtils.UI.VerificationLinkLayout.GO_BACK_TO_LOGIN_BUTTON_TEXT, null, locale),
                event -> UI.getCurrent().navigate(RouteUtils.LOGIN_ROUTE)
        );
        goBackToLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(goBackToLogin);
        return buttonLayout;
    }
}
