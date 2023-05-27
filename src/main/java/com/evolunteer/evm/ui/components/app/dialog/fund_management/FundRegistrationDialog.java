package com.evolunteer.evm.ui.components.app.dialog.fund_management;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundRequisiteDto;
import com.evolunteer.evm.common.domain.dto.general.Pair;
import com.evolunteer.evm.common.domain.enums.fund_management.FundActivityCategory;
import com.evolunteer.evm.common.domain.request.CreateFundRequest;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.components.app.layout.form.address_management.AddressFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundRequisiteFormLayout;
import com.evolunteer.evm.ui.components.general.button.CancelButton;
import com.evolunteer.evm.ui.components.general.button.DeleteButton;
import com.evolunteer.evm.ui.components.general.button.SaveButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.*;
import java.util.stream.Collectors;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.Error.*;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.*;

public class FundRegistrationDialog extends Dialog {

    private final Binder<CreateFundRequest> createFundRequestBinder;
    private final Binder<AddressDto> addressDtoBinder;
    private final List<Binder<FundRequisiteDto>> requisiteBinders;
    private final CreateFundRequest createFundRequest;
    private final AddressDto address;
    private final MessageSource messageSource;
    private final Locale locale;
    private final FundService fundService;

    public FundRegistrationDialog(MessageSource messageSource, Locale locale, FundService fundService) {
        this.fundService = fundService;
        this.messageSource = messageSource;
        this.locale = locale;
        this.createFundRequest = new CreateFundRequest();
        this.address = new AddressDto();
        this.createFundRequestBinder = new Binder<>();
        this.addressDtoBinder = new Binder<>();
        this.requisiteBinders = new ArrayList<>();

        final H3Header registrationFormHeader = new H3Header(messageSource, locale, HEADER_TEXT);

        createFundRequestBinder.setBean(createFundRequest);
        addressDtoBinder.setBean(address);

        final FormLayout registrationFundForm = new FormLayout();
        registrationFundForm.add(
                registrationFormHeader,
                new Hr(),
                this.mainInfoBlock(),
                new Hr(),
                this.communicationBlock(),
                new Hr(),
                new AddressFormLayout(messageSource, locale, addressDtoBinder),
                new Hr(),
                new FundRequisiteFormLayout(messageSource, locale, requisiteBinders)
        );
        registrationFundForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        add(registrationFundForm);

        this.getFooter().add(
                new SaveButton(messageSource, locale, this.saveFund()),
                new CancelButton(messageSource, locale, buttonClickEvent -> this.close()));
    }

    private ComponentEventListener<ClickEvent<Button>> saveFund() {
        return event -> {
            final String fundSuccessfullyRegistered = messageSource.getMessage(SUCCESSFULLY_FUND_REGISTRATION, null, locale);
            if(
                    createFundRequestBinder.writeBeanIfValid(createFundRequest)
                    && addressDtoBinder.writeBeanIfValid(address)
                    && requisiteBinders.stream().allMatch(binder -> binder.validate().isOk())
            ) {

                final Set<FundRequisiteDto> requisites = requisiteBinders.stream()
                        .map(requisiteBinder -> {
                            final FundRequisiteDto requisite = new FundRequisiteDto();
                            requisiteBinder.writeBeanIfValid(requisite);
                            return requisite;
                        }).collect(Collectors.toSet());
                createFundRequest.setRequisites(requisites);
                createFundRequest.setAddress(address);
                fundService.createFund(createFundRequest);
                NotificationFactory.success(fundSuccessfullyRegistered).open();
                this.close();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                UI.getCurrent().getPage().reload();
            }
        };
    }

    private FormLayout communicationBlock() {
        final String phoneFieldText = messageSource.getMessage(PHONE_FIELD_TEXT, null, locale);
        final String emailFieldText = messageSource.getMessage(EMAIL_FIELD_TEXT, null, locale);

        final String phoneValidationText = messageSource.getMessage(VALIDATION_PHONE_ERROR, null, locale);
        final String emailValidationText = messageSource.getMessage(VALIDATION_EMAIL_ERROR, null, locale);

        final H3Header communicationHeader = new H3Header(messageSource, locale, COMMUNICATIONS_HEADER_TEXT);

        final TextField phoneField = new TextField(phoneFieldText);
        phoneField.setRequired(true);
        phoneField.setRequiredIndicatorVisible(true);
        createFundRequestBinder.forField(phoneField)
                .withValidator(new RegexpValidator(phoneValidationText, ValidationUtils.NUMBER_REGEX))
                .bind(CreateFundRequest::getPhone, CreateFundRequest::setPhone);

        final EmailField emailField = new EmailField(emailFieldText);
        emailField.setRequiredIndicatorVisible(true);
        createFundRequestBinder.forField(emailField)
                .withValidator(new RegexpValidator(emailValidationText, ValidationUtils.EMAIL_REGEX))
                .bind(CreateFundRequest::getEmail, CreateFundRequest::setEmail);

        final FormLayout communicationForm = new FormLayout();
        communicationForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        communicationForm.add(
                communicationHeader,
                phoneField,
                emailField
        );
        return communicationForm;
    }

    private FormLayout mainInfoBlock() {
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
        createFundRequestBinder.forField(nameField)
                .withValidator(name -> !StringUtils.isBlank(name), nameValidationText)
                .bind(CreateFundRequest::getName, CreateFundRequest::setName);

        final int textLimit = 255;
        final TextArea descriptionField = new TextArea(descriptionFieldText);
        descriptionField.setWidthFull();
        descriptionField.setMaxLength(textLimit);
        descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
        descriptionField.addValueChangeListener(e -> {e.getSource().setHelperText(e.getValue().length() + "/" + textLimit);
        });
        descriptionField.setRequired(true);
        descriptionField.setRequiredIndicatorVisible(true);
        createFundRequestBinder.forField(descriptionField)
                .withValidator(description -> !StringUtils.isBlank(description), descriptionValidationText)
                .bind(CreateFundRequest::getDescription, CreateFundRequest::setDescription);

        final MultiSelectComboBox<FundActivityCategory> categoriesField = new MultiSelectComboBox<>(categoriesFieldText);
        categoriesField.setItems(FundActivityCategory.values());
        categoriesField.setItemLabelGenerator(item -> messageSource.getMessage(item.getLocalizedValue(), null, locale));

        categoriesField.setRequired(true);
        categoriesField.setRequiredIndicatorVisible(true);
        createFundRequestBinder.forField(categoriesField)
                .withValidator(categories -> categories.size() >= 1, categoriesValidationText)
                .bind(CreateFundRequest::getCategories, CreateFundRequest::setCategories);

        final FormLayout mainInfoForm = new FormLayout();
        mainInfoForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        mainInfoForm.add(
                mainInfoHeader,
                nameField,
                descriptionField,
                categoriesField
        );
        return mainInfoForm;
    }
}
