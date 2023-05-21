package com.evolunteer.evm.ui.components.general.header;

import com.vaadin.flow.component.html.H1;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class H1Header extends H1 {

    public H1Header(MessageSource messageSource, Locale locale, String text) {
        this.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        this.setText(messageSource.getMessage(text, null, locale));
    }
}
