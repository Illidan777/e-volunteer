package com.evolunteer.evm.ui.components.app.view.visitor;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.dto.fund_management.FundHelpRequestDto;
import com.evolunteer.evm.common.domain.dto.fund_management.FundRequisiteDto;
import com.evolunteer.evm.common.domain.dto.fund_management.HelpRequestExecutorDto;
import com.evolunteer.evm.common.domain.enums.fund_management.FundActivityCategory;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.string.StringSymbolUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.components.app.dialog.fund_management.FundCheckHelpRequestStatusDialog;
import com.evolunteer.evm.ui.components.app.dialog.fund_management.FundHelpRequestDialog;
import com.evolunteer.evm.ui.components.app.select.LanguageSelect;
import com.evolunteer.evm.ui.components.general.button.CancelButton;
import com.evolunteer.evm.ui.components.general.button.SearchButton;
import com.evolunteer.evm.ui.components.general.button.SendButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.header.H4Header;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.CATEGORY_FIELD_TEXT;
import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.NAME_FIELD_TEXT;
import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.HYPHEN;
import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.SPACE;

@Route(RouteUtils.VISITOR_ROUTE)
public class VisitorView extends VerticalLayout {

    private final MessageSource messageSource;
    private final Locale locale;
    private final FundService fundService;

    public VisitorView(MessageSource messageSource, FundService fundService) {
        this.locale = LocalizationUtils.getLocale();
        this.messageSource = messageSource;
        this.fundService = fundService;
        this.add(new LanguageSelect(messageSource));

        final String categoriesFieldText = messageSource.getMessage(CATEGORY_FIELD_TEXT, null, locale);
        final String nameFieldText = messageSource.getMessage(NAME_FIELD_TEXT, null, locale);
        final String checkRequestStatusButtonText = messageSource.getMessage(LocalizationUtils.UI.VisitorView.CHECK_REQUEST_BUTTON_TEXT, null, locale);

        final VerticalLayout navigationLayout = new VerticalLayout();
        navigationLayout.setSizeFull();
        navigationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        navigationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        final FormLayout fieldsLayout = new FormLayout();
        fieldsLayout.setSizeFull();

        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeFull();
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        final MultiSelectComboBox<FundActivityCategory> fundCategoriesField = new MultiSelectComboBox<>(categoriesFieldText);
        fundCategoriesField.setItems(FundActivityCategory.values());
        fundCategoriesField.setItemLabelGenerator(item -> messageSource.getMessage(item.getLocalizedValue(), null, locale));

        final TextField fundNameField = new TextField(nameFieldText);

        final HorizontalLayout fundsLayout = new HorizontalLayout();
        fundsLayout.setSpacing(true);
        fundsLayout.setPadding(true);
        final SearchButton findFund = new SearchButton(messageSource, locale, event -> {
            if (fundsLayout.isAttached()) {
                this.remove(fundsLayout);
                fundsLayout.removeAll();
            }

            final Set<FundDtoFull> funds = fundService.findFundByNameAndCategories(fundNameField.getValue(), fundCategoriesField.getValue());
            if (funds.isEmpty()) {
                fundsLayout.setSizeFull();
                fundsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                fundsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

                final Div noFundsSearchResultDiv = new Div(new H4Header(messageSource, locale, LocalizationUtils.UI.VisitorView.NO_FUND_SEARCH_RESULT));
                noFundsSearchResultDiv.setWidth("100%");
                noFundsSearchResultDiv.getStyle().set("text-align", "center");
                fundsLayout.add(noFundsSearchResultDiv);
            } else {
                funds.forEach(fund -> fundsLayout.add(this.fundDiv(fund)));
            }
            this.add(fundsLayout);
        });
        findFund.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        final Button checkRequestStatus = new Button(checkRequestStatusButtonText, event -> new FundCheckHelpRequestStatusDialog(messageSource, locale, fundService).open());
        checkRequestStatus.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        fieldsLayout.add(fundCategoriesField, fundNameField);
        buttonLayout.add(findFund, checkRequestStatus);

        navigationLayout.add(new H3Header(messageSource, locale, LocalizationUtils.UI.VisitorView.HEADER_TEXT), new Hr(), fieldsLayout, buttonLayout, new Hr());

        this.add(navigationLayout);
    }

    private Div fundDiv(final FundDtoFull fundDtoFull) {
        final String applyForFundButtonText = messageSource.getMessage(LocalizationUtils.UI.VisitorView.APPLY_FOR_FUND_BUTTON_TEXT, null, locale);

        final String moreInfoDetailsText = messageSource.getMessage(LocalizationUtils.UI.CommonText.MORE_INFO_DETAILS_TEXT, null, locale);
        final String requisitesDetailsText = messageSource.getMessage(LocalizationUtils.UI.VisitorView.FUND_REQUISITES_DETAILS_TEXT, null, locale);
        final String phoneInfoText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.PHONE_FIELD_TEXT, null, locale);
        final String emailInfoText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.EMAIL_FIELD_TEXT, null, locale);
        final String descriptionInfoText = messageSource.getMessage(LocalizationUtils.UI.FundProfileView.DESCRIPTION_FIELD_TEXT, null, locale);
        final String categoriesFieldText = messageSource.getMessage(CATEGORY_FIELD_TEXT, null, locale);

        final String recipientFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_RECIPIENT_FIELD_TEXT, null, locale);
        final String bankFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_BANK_FIELD_TEXT, null, locale);
        final String bankCodeFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_BANK_CODE_FIELD_TEXT, null, locale);
        final String ibanFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_IBAN_FIELD_TEXT, null, locale);
        final String paymentAccountFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_PAYMENT_ACCOUNT_FIELD_TEXT, null, locale);
        final String swiftCodeFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_SWIFT_CODE_FIELD_TEXT, null, locale);
        final String legalAddressFieldText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_LEGAL_ADDRESS_FIELD_TEXT, null, locale);
        final String paymentLinkText = messageSource.getMessage(LocalizationUtils.UI.FundRegistrationDialog.REQUISITE_PAYMENT_LINK_FIELD_TEXT, null, locale);


        final VerticalLayout fundDetailsLayout = new VerticalLayout();

        final Details fundGeneralInfoDetails = new Details(moreInfoDetailsText);
        final Details fundRequisitesDetails = new Details(requisitesDetailsText);

        final Span phoneSpan = new Span(phoneInfoText + SPACE + fundDtoFull.getPhone());
        final Span emailSpan = new Span(emailInfoText + SPACE + fundDtoFull.getEmail());
        final Span descriptionSpan = new Span(descriptionInfoText + SPACE + fundDtoFull.getDescription());
        final Span categoriesSpan = new Span(categoriesFieldText + SPACE + fundDtoFull.getCategories().stream()
                .map(category -> messageSource.getMessage(category.getLocalizedValue(), null, locale))
                .collect(Collectors.joining(StringSymbolUtils.COMMA + StringSymbolUtils.SPACE)));

        final VerticalLayout fundGeneralDetailsContent = new VerticalLayout(phoneSpan, emailSpan, descriptionSpan, categoriesSpan);
        fundGeneralDetailsContent.setSpacing(false);
        fundGeneralDetailsContent.setPadding(false);
        fundGeneralInfoDetails.addContent(fundGeneralDetailsContent);

        final Set<FundRequisiteDto> requisites = fundDtoFull.getRequisites();
        final VerticalLayout fundRequisiteDetailsContent = new VerticalLayout();
        requisites.forEach(requisite -> {
            final VerticalLayout requisiteDiv = new VerticalLayout();

            final Span recipientSpan = new Span(recipientFieldText + SPACE + requisite.getRecipient());
            final Span bankSpan = new Span(bankFieldText + SPACE + requisite.getBank());
            final Span bankCodeSpan = new Span(bankCodeFieldText + SPACE + requisite.getBankCode());
            final Span ibanSpan = new Span(ibanFieldText + SPACE + (Objects.isNull(requisite.getIban()) ? HYPHEN : requisite.getIban()));
            final Span paymentAccountSpan = new Span(paymentAccountFieldText + SPACE + requisite.getPaymentAccount());
            final Span swiftCodeSpan = new Span(swiftCodeFieldText + SPACE + (Objects.isNull(requisite.getSwiftCode()) ? HYPHEN : requisite.getSwiftCode()));
            final Span legalAddressSpan = new Span(legalAddressFieldText + SPACE + (Objects.isNull(requisite.getLegalAddress()) ? HYPHEN : requisite.getLegalAddress()));
            final Span paymentLinkSpan = new Span(paymentLinkText + SPACE + (Objects.isNull(requisite.getPaymentLink()) ? HYPHEN : requisite.getPaymentLink()));
            requisiteDiv.add(
                    recipientSpan,
                    bankSpan,
                    bankCodeSpan,
                    ibanSpan,
                    paymentAccountSpan,
                    swiftCodeSpan,
                    legalAddressSpan,
                    paymentLinkSpan
            );
            fundRequisiteDetailsContent.add(requisiteDiv, new Hr());
        });

        fundRequisitesDetails.addContent(fundRequisiteDetailsContent);

        fundDetailsLayout.add(new H4Header(fundDtoFull.getName()), fundGeneralInfoDetails, fundRequisitesDetails);

        final Button applyButton = new Button(applyForFundButtonText, new Icon(VaadinIcon.DOCTOR), event -> new FundHelpRequestDialog(messageSource, locale, fundService, fundDtoFull).open());
        applyButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return new Div(fundDetailsLayout, applyButton);
    }
}
