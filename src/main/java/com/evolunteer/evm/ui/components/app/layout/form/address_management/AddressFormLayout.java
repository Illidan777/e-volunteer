package com.evolunteer.evm.ui.components.app.layout.form.address_management;

import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class AddressFormLayout extends FormLayout {

    public AddressFormLayout(MessageSource messageSource, Locale locale, Binder<AddressDto> addressBinder) {

        final String countryFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.COUNTRY_FIELD_TEXT, null, locale);
        final String regionFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.REGION_FIELD_TEXT, null, locale);
        final String cityFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.CITY_FIELD_TEXT, null, locale);
        final String streetFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.STREET_FIELD_TEXT, null, locale);
        final String houseFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.HOUSE_FIELD_TEXT, null, locale);
        final String corpusFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.CORPUS_FIELD_TEXT, null, locale);
        final String officeFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.OFFICE_FIELD_TEXT, null, locale);
        final String postIndexFieldText = messageSource.getMessage(LocalizationUtils.UI.Address.POST_INDEX_FIELD_TEXT, null, locale);

        final String countryValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_COUNTRY_ERROR, null, locale);
        final String regionValidationErrorText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_REGION_ERROR, null, locale);
        final String cityValidationErrorText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_CITY_ERROR, null, locale);
        final String streetValidationErrorText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_STREET_ERROR, null, locale);
        final String houseValidationErrorText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_HOUSE_ERROR, null, locale);

        final H3Header addressHeader = new H3Header(messageSource, locale, LocalizationUtils.UI.Address.HEADER_TEXT);

        final TextField countryField = new TextField(countryFieldText);
        countryField.setRequired(true);
        countryField.setRequiredIndicatorVisible(true);
        addressBinder.forField(countryField)
                .withValidator(country -> !StringUtils.isBlank(country), countryValidationText)
                .bind(AddressDto::getCountry, AddressDto::setCountry);

        final TextField regionField = new TextField(regionFieldText);
        regionField.setRequired(true);
        regionField.setRequiredIndicatorVisible(true);
        addressBinder.forField(regionField)
                .withValidator(region -> !StringUtils.isBlank(region), regionValidationErrorText)
                .bind(AddressDto::getRegion, AddressDto::setRegion);

        final TextField cityField = new TextField(cityFieldText);
        cityField.setRequired(true);
        cityField.setRequiredIndicatorVisible(true);
        addressBinder.forField(cityField)
                .withValidator(city -> !StringUtils.isBlank(city), cityValidationErrorText)
                .bind(AddressDto::getCity, AddressDto::setCity);

        final TextField streetField = new TextField(streetFieldText);
        streetField.setRequired(true);
        streetField.setRequiredIndicatorVisible(true);
        addressBinder.forField(streetField)
                .withValidator(street -> !StringUtils.isBlank(street), streetValidationErrorText)
                .bind(AddressDto::getStreet, AddressDto::setStreet);

        final TextField houseField = new TextField(houseFieldText);
        houseField.setRequired(true);
        houseField.setRequiredIndicatorVisible(true);
        addressBinder.forField(houseField)
                .withValidator(house -> !StringUtils.isBlank(house), houseValidationErrorText)
                .bind(AddressDto::getHouse, AddressDto::setHouse);

        final TextField corpusField = new TextField(corpusFieldText);
        addressBinder.forField(corpusField)
                .bind(AddressDto::getCorpus, AddressDto::setCorpus);

        final TextField officeField = new TextField(officeFieldText);
        addressBinder.forField(officeField)
                .bind(AddressDto::getOffice, AddressDto::setOffice);

        final TextField postIndexField = new TextField(postIndexFieldText);
        addressBinder.forField(postIndexField)
                .bind(AddressDto::getPostIndex, AddressDto::setPostIndex);


        this.add(addressHeader, countryField, regionField, cityField,
                streetField, houseField, corpusField, officeField, postIndexField);
        this.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
    }
}
