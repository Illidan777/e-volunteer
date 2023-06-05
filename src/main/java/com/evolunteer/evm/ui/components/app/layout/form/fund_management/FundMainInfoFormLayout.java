package com.evolunteer.evm.ui.components.app.layout.form.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import com.evolunteer.evm.common.domain.enums.fund_management.FundActivityCategory;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.*;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.*;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.MAIN_INFO_HEADER_TEXT;

public class FundMainInfoFormLayout <T extends BaseFundDto> extends FormLayout {

    public FundMainInfoFormLayout(MessageSource messageSource, Locale locale, Binder<T> fundBinder) {
        final String nameFieldText = messageSource.getMessage(NAME_FIELD_TEXT, null, locale);
        final String descriptionFieldText = messageSource.getMessage(DESCRIPTION_FIELD_TEXT, null, locale);
        final String categoriesFieldText = messageSource.getMessage(CATEGORY_FIELD_TEXT, null, locale);

        final String nameValidationText = messageSource.getMessage(VALIDATION_FUND_NAME_ERROR, null, locale);
        final String descriptionValidationText = messageSource.getMessage(VALIDATION_FUND_DESCRIPTION_ERROR, null, locale);
        final String categoriesValidationText = messageSource.getMessage(VALIDATION_FUND_CATEGORIES_ERROR, null, locale);

        final H3Header mainInfoHeader = new H3Header(messageSource, locale, MAIN_INFO_HEADER_TEXT);

        final TextField nameField = new TextField(nameFieldText);
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);
        fundBinder.forField(nameField)
                .withValidator(name -> !StringUtils.isBlank(name), nameValidationText)
                .bind(T::getName, T::setName);

        final int textLimit = 255;
        final TextArea descriptionField = new TextArea(descriptionFieldText);
        descriptionField.setWidthFull();
        descriptionField.setMaxLength(textLimit);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.addValueChangeListener(e -> {
            e.getSource().setHelperText(e.getValue().length() + "/" + textLimit);
        });
        descriptionField.setRequired(true);
        descriptionField.setRequiredIndicatorVisible(true);
        fundBinder.forField(descriptionField)
                .withValidator(description -> !StringUtils.isBlank(description), descriptionValidationText)
                .bind(T::getDescription, T::setDescription);

        final MultiSelectComboBox<FundActivityCategory> categoriesField = new MultiSelectComboBox<>(categoriesFieldText);
        categoriesField.setItems(FundActivityCategory.values());
        categoriesField.setItemLabelGenerator(item -> messageSource.getMessage(item.getLocalizedValue(), null, locale));

        categoriesField.setRequired(true);
        categoriesField.setRequiredIndicatorVisible(true);
        fundBinder.forField(categoriesField)
                .withValidator(categories -> categories.size() >= 1, categoriesValidationText)
                .bind(T::getCategories, T::setCategories);

        this.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        this.add(
                mainInfoHeader,
                nameField,
                descriptionField,
                categoriesField
        );
    }
}
