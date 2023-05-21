package com.evolunteer.evm.ui.components.app.layout;

import com.evolunteer.evm.ui.components.app.select.LanguageSelect;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.context.MessageSource;

public class LanguageLayout extends VerticalLayout {

    public LanguageLayout(MessageSource messageSource) {
        this.setAlignItems(FlexComponent.Alignment.END);
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        this.setSpacing(true);
        this.setMargin(true);
        add(new LanguageSelect(messageSource));
    }
}