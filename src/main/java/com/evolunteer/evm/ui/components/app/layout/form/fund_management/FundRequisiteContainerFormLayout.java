package com.evolunteer.evm.ui.components.app.layout.form.fund_management;

import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class FundRequisiteContainerFormLayout extends FormLayout {

    public FundRequisiteContainerFormLayout(MessageSource messageSource,
                                            Locale locale,
                                            ComponentEventListener<ClickEvent<Button>> addRequisiteButtonClickListener) {

        final H3Header requisiteFormHeader = new H3Header(messageSource, locale, LocalizationUtils.UI.FundRegistrationDialog.REQUISITES_HEADER_TEXT);
        final Button addRequisiteButton = new Button(new Icon(VaadinIcon.PLUS));

        this.add(
                requisiteFormHeader,
                addRequisiteButton
        );
        this.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        addRequisiteButton.addClickListener(addRequisiteButtonClickListener);
    }
}
