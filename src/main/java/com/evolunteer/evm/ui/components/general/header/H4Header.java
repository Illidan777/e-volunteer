package com.evolunteer.evm.ui.components.general.header;

import com.vaadin.flow.component.html.H4;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class H4Header extends H4 {

    public H4Header(String text) {
        this.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        this.setText(text);
    }

    public H4Header(MessageSource messageSource, Locale locale, String text) {
        this.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        this.setText(messageSource.getMessage(text, null, locale));
    }
}
