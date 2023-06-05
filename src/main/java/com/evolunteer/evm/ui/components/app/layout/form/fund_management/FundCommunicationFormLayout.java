package com.evolunteer.evm.ui.components.app.layout.form.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_EMAIL_ERROR;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.VALIDATION_PHONE_ERROR;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.*;

public class FundCommunicationFormLayout<T extends BaseFundDto> extends FormLayout {

    public FundCommunicationFormLayout(MessageSource messageSource, Locale locale, Binder<T> fundBinder) {
        final String phoneFieldText = messageSource.getMessage(PHONE_FIELD_TEXT, null, locale);
        final String emailFieldText = messageSource.getMessage(EMAIL_FIELD_TEXT, null, locale);

        final String phoneValidationText = messageSource.getMessage(VALIDATION_PHONE_ERROR, null, locale);
        final String emailValidationText = messageSource.getMessage(VALIDATION_EMAIL_ERROR, null, locale);

        final H3Header communicationHeader = new H3Header(messageSource, locale, COMMUNICATIONS_HEADER_TEXT);

        final TextField phoneField = new TextField(phoneFieldText);
        phoneField.setRequired(true);
        phoneField.setRequiredIndicatorVisible(true);
        fundBinder.forField(phoneField)
                .withValidator(new RegexpValidator(phoneValidationText, ValidationUtils.NUMBER_REGEX))
                .bind(T::getPhone, T::setPhone);

        final EmailField emailField = new EmailField(emailFieldText);
        emailField.setRequiredIndicatorVisible(true);
        fundBinder.forField(emailField)
                .withValidator(new RegexpValidator(emailValidationText, ValidationUtils.EMAIL_REGEX))
                .bind(T::getEmail, T::setEmail);
        this.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        this.add(
                communicationHeader,
                phoneField,
                emailField
        );
    }
}
