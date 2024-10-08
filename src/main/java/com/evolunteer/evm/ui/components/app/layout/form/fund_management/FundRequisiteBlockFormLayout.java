package com.evolunteer.evm.ui.components.app.layout.form.fund_management;

import com.evolunteer.evm.common.domain.dto.fund_management.FundRequisiteDto;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.general.button.DeleteButton;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class FundRequisiteBlockFormLayout extends FormLayout {

    public FundRequisiteBlockFormLayout(MessageSource messageSource, Locale locale, Binder<FundRequisiteDto> fundRequisiteBinder) {

        final String recipientFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_RECIPIENT_FIELD_TEXT, null, locale);
        final String bankFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_BANK_FIELD_TEXT, null, locale);
        final String bankCodeFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_BANK_CODE_FIELD_TEXT, null, locale);
        final String ibanFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_IBAN_FIELD_TEXT, null, locale);
        final String paymentAccountFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_PAYMENT_ACCOUNT_FIELD_TEXT, null, locale);
        final String swiftCodeFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_SWIFT_CODE_FIELD_TEXT, null, locale);
        final String legalAddressFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_LEGAL_ADDRESS_FIELD_TEXT, null, locale);
        final String paymentLinkText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_PAYMENT_LINK_FIELD_TEXT, null, locale);

        final String recipientValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_REQUISITE_RECIPIENT_ERROR, null, locale);
        final String bankValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_REQUISITE_BANK_ERROR, null, locale);
        final String bankCodeValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_REQUISITE_BANK_CODE_ERROR, null, locale);
        final String paymentAccountValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_REQUISITE_PAYMENT_ACCOUNT_ERROR, null, locale);
        final String legalAddressValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_REQUISITE_LEGAL_ADDRESS_ERROR, null, locale);

        final TextField recipientField = new TextField(recipientFieldText);
        recipientField.setRequired(true);
        recipientField.setRequiredIndicatorVisible(true);
        fundRequisiteBinder.forField(recipientField)
                .withValidator(recipient -> !StringUtils.isBlank(recipient), recipientValidationText)
                .bind(FundRequisiteDto::getRecipient, FundRequisiteDto::setRecipient);

        final TextField legalAddressField = new TextField(legalAddressFieldText);
        legalAddressField.setRequired(true);
        legalAddressField.setRequiredIndicatorVisible(true);
        fundRequisiteBinder.forField(legalAddressField)
                .withValidator(legalAddress -> !StringUtils.isBlank(legalAddress), legalAddressValidationText)
                .bind(FundRequisiteDto::getLegalAddress, FundRequisiteDto::setLegalAddress);

        final TextField bankField = new TextField(bankFieldText);
        bankField.setRequired(true);
        bankField.setRequiredIndicatorVisible(true);
        fundRequisiteBinder.forField(bankField)
                .withValidator(bank -> !StringUtils.isBlank(bank), bankValidationText)
                .bind(FundRequisiteDto::getBank, FundRequisiteDto::setBank);

        final TextField bankCodeField = new TextField(bankCodeFieldText);
        bankCodeField.setRequired(true);
        bankCodeField.setRequiredIndicatorVisible(true);
        fundRequisiteBinder.forField(bankCodeField)
                .withValidator(bankCode -> !StringUtils.isBlank(bankCode), bankCodeValidationText)
                .bind(FundRequisiteDto::getBankCode, FundRequisiteDto::setBankCode);

        final TextField paymentAccountField = new TextField(paymentAccountFieldText);
        paymentAccountField.setRequired(true);
        paymentAccountField.setRequiredIndicatorVisible(true);
        fundRequisiteBinder.forField(paymentAccountField)
                .withValidator(paymentAccount -> !StringUtils.isBlank(paymentAccount), paymentAccountValidationText)
                .bind(FundRequisiteDto::getPaymentAccount, FundRequisiteDto::setPaymentAccount);

        final TextField ibanField = new TextField(ibanFieldText);
        fundRequisiteBinder.forField(ibanField)
                .bind(FundRequisiteDto::getIban, FundRequisiteDto::setIban);

        final TextField swiftCodeField = new TextField(swiftCodeFieldText);
        fundRequisiteBinder.forField(swiftCodeField)
                .bind(FundRequisiteDto::getSwiftCode, FundRequisiteDto::setSwiftCode);

        final TextField paymentLinkField = new TextField(paymentLinkText);
        fundRequisiteBinder.forField(paymentLinkField)
                .bind(FundRequisiteDto::getPaymentLink, FundRequisiteDto::setPaymentLink);

        this.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        this.add(
                recipientField,
                legalAddressField,
                bankField,
                bankCodeField,
                paymentAccountField,
                ibanField,
                swiftCodeField,
                paymentLinkField
        );
    }
}
