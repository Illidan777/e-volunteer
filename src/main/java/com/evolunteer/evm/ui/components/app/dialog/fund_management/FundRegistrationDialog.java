package com.evolunteer.evm.ui.components.app.dialog.fund_management;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundRequisiteDto;
import com.evolunteer.evm.common.domain.request.fund_management.CreateFundRequest;
import com.evolunteer.evm.ui.components.app.layout.form.address_management.AddressFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundCommunicationFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundMainInfoFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundRequisiteBlockFormLayout;
import com.evolunteer.evm.ui.components.app.layout.form.fund_management.FundRequisiteContainerFormLayout;
import com.evolunteer.evm.ui.components.general.button.CancelButton;
import com.evolunteer.evm.ui.components.general.button.DeleteButton;
import com.evolunteer.evm.ui.components.general.button.SaveButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.HEADER_TEXT;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.SUCCESSFULLY_FUND_REGISTRATION;

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
                new FundMainInfoFormLayout<>(messageSource, locale, createFundRequestBinder),
                new Hr(),
                new FundCommunicationFormLayout<>(messageSource, locale, createFundRequestBinder),
                new Hr(),
                new AddressFormLayout(messageSource, locale, addressDtoBinder),
                new Hr(),
                new FundRequisiteContainerFormLayout(messageSource, locale, this.addRequisiteButtonClickListener(messageSource, locale, requisiteBinders))
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
            if (
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

    private ComponentEventListener<ClickEvent<Button>> addRequisiteButtonClickListener(MessageSource messageSource, Locale locale, List<Binder<FundRequisiteDto>> requisiteBinders) {
        return event -> {
            final Binder<FundRequisiteDto> fundRequisiteBinder = new Binder<>();
            final FundRequisiteBlockFormLayout requisiteBlockFormLayout = new FundRequisiteBlockFormLayout(messageSource, locale, fundRequisiteBinder);
            final DeleteButton removeRequisiteBlockButton = new DeleteButton(messageSource, locale, deleteEvent -> {
                if (requisiteBlockFormLayout.isAttached()) {
                    this.remove(requisiteBlockFormLayout);
                    this.requisiteBinders.remove(fundRequisiteBinder);
                }
            });
            requisiteBlockFormLayout.add(removeRequisiteBlockButton);
            this.add(requisiteBlockFormLayout);
            requisiteBinders.add(fundRequisiteBinder);
        };
    }
}
