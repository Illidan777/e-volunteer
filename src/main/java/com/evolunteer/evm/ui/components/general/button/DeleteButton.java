package com.evolunteer.evm.ui.components.general.button;

import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class DeleteButton extends Button{

    private final Locale locale;
    private final MessageSource messageSource;

    public DeleteButton(MessageSource messageSource, Locale locale, ComponentEventListener<ClickEvent<Button>> clickListener) {
        this.locale = locale;
        this.messageSource = messageSource;

        this.init();
        this.addClickListener(clickListener);
    }

    private void init() {
        this.addThemeVariants(ButtonVariant.LUMO_ERROR);
        final String messageText = messageSource.getMessage(LocalizationUtils.UI.CommonText.DELETE_BUTTON_TEXT, null, locale);
        this.setIcon(new Icon(VaadinIcon.BAN));
        this.setText(messageText);
    }
}
