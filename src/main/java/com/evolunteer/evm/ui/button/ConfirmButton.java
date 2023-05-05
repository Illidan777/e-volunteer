package com.evolunteer.evm.ui.button;

import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class ConfirmButton extends Button {

    public ConfirmButton(MessageSource messageSource, Locale locale, ComponentEventListener<ClickEvent<Button>> clickListener) {
        this.init(messageSource, locale);
        this.addClickListener(clickListener);
    }

    private void init(MessageSource messageSource, Locale locale) {
        this.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        final String messageText = messageSource.getMessage(LocalizationUtils.UI.CommonText.CONFIRM_BUTTON_TEXT, null, locale);
        this.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));
        this.setText(messageText);
    }
}
