package com.evolunteer.evm.ui.components.general.header;

import com.vaadin.flow.component.html.H3;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class H3Header extends H3 {

    public H3Header(MessageSource messageSource, Locale locale, String text) {
        this.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        this.setText(messageSource.getMessage(text, null, locale));
    }
}
