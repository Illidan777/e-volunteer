package com.evolunteer.evm.ui.components.app.dialog.fund_management;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.common.domain.dto.fund_management.FundHelpRequestDto;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.general.button.CancelButton;
import com.evolunteer.evm.ui.components.general.button.ConfirmButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;

public class FundCheckHelpRequestStatusDialog extends Dialog {

    private final MessageSource messageSource;
    private final Locale locale;
    private final Binder<String> requestNumberBinder;
    private final TextField requestNumberField;
    private final FundService fundService;

    public FundCheckHelpRequestStatusDialog(MessageSource messageSource, Locale locale, FundService fundService) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.requestNumberBinder = new Binder<>();
        this.fundService = fundService;

        final String requestNumberFieldText = messageSource.getMessage(LocalizationUtils.UI.VisitorView.REQUEST_NUMBER_FIELD_TEXT, null, locale);
        final String requestNumberValidationError = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_HELP_REQUEST_NUMBER_ERROR, null, locale);

        requestNumberField = new TextField(requestNumberFieldText);
        requestNumberField.setRequired(true);
        requestNumberField.setRequiredIndicatorVisible(true);
        requestNumberBinder.forField(requestNumberField)
                .withValidator(value -> !StringUtils.isBlank(value), requestNumberValidationError)
                .bind(value -> requestNumberField.getValue(), (name, value) -> requestNumberField.setValue(value));

        final ConfirmButton confirmButton = new ConfirmButton(messageSource, locale, this.checkHelpRequestStatusByNumber());

        final FormLayout checkRequestStatusFormLayout = new FormLayout();

        checkRequestStatusFormLayout.add(requestNumberField);
        checkRequestStatusFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));

        add(checkRequestStatusFormLayout);
        this.getFooter().add(confirmButton, new CancelButton(messageSource, locale, buttonClickEvent -> this.close()));
    }

    private ComponentEventListener<ClickEvent<Button>> checkHelpRequestStatusByNumber() {
        return event -> {
            if (requestNumberBinder.validate().isOk()) {
                final String requestNumber = requestNumberField.getValue();
                final String requestNotFoundError = messageSource.getMessage(LocalizationUtils.Error.NOT_FOUND_HELP_REQUEST_NUMBER_ERROR,
                        new String[]{requestNumber}, locale);

                final Optional<FundHelpRequestDto> optionalFundHelpRequestDto = fundService.getFundHelpRequestByNumber(requestNumber);
                if (optionalFundHelpRequestDto.isEmpty()) {
                    NotificationFactory.error(requestNotFoundError).open();
                } else {
                    this.removeAll();
                    this.getFooter().removeAll();

                    final FundHelpRequestDto fundHelpRequestDto = optionalFundHelpRequestDto.get();

                    final Div requestStatusDiv = new Div();
                    requestStatusDiv.setWidth("100%");
                    requestStatusDiv.getStyle().set("text-align", "center");
                    requestStatusDiv.setText(messageSource.getMessage(fundHelpRequestDto.getStatus().getLocalizedMessage(), null, locale));
                    this.add(requestStatusDiv);
                }
            }
        };
    }
}
