package com.evolunteer.evm.ui.components.app.view.cabinet;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.fund_management.FundDtoFull;
import com.evolunteer.evm.common.domain.dto.fund_management.FundHelpRequestDto;
import com.evolunteer.evm.common.domain.dto.fund_management.HelpRequestExecutorDto;
import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.enums.fund_management.FundHelpRequestStatus;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.string.StringSymbolUtils;
import com.evolunteer.evm.ui.components.app.layout.navigation.ParentNavigationLayout;
import com.evolunteer.evm.ui.components.general.header.H4Header;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.ApplicationsView.*;
import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.SPACE;

@Route(value = RouteUtils.APPLICATIONS_ROUTE, layout = ParentNavigationLayout.class)
public class ApplicationsView extends VerticalLayout {

    private final Locale locale;
    private final MessageSource messageSource;

    public ApplicationsView(MessageSource messageSource, FundService fundService, UserService userService) {
        this.locale = LocalizationUtils.getLocale();
        this.messageSource = messageSource;

        final BaseUserDto contextUser = userService.getContextUser();
        final FundDtoFull fund = fundService.getFundById(contextUser.getFund().getId());

        final String requestNumberHeader = messageSource.getMessage(GRID_REQUEST_NUMBER_HEADER_TEXT, null, locale);
        final String requestStatusHeader = messageSource.getMessage(GRID_REQUEST_STATUS_HEADER_TEXT, null, locale);
        final String requestCreatedAtHeader = messageSource.getMessage(GRID_REQUEST_CREATE_AT_HEADER_TEXT, null, locale);
        final String requestDetailsHeader = messageSource.getMessage(GRID_REQUEST_DETAILS_HEADER_TEXT, null, locale);
        final String requestExecutorHeader = messageSource.getMessage(GRID_REQUEST_EXECUTOR_HEADER_TEXT, null, locale);

        final ListDataProvider<FundHelpRequestDto> dataProvider = new ListDataProvider<>(fund.getHelpRequests());

        final Grid<FundHelpRequestDto> grid = new Grid<>(FundHelpRequestDto.class, false);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addColumn(FundHelpRequestDto::getNumber).setHeader(requestNumberHeader).setSortable(true);
        grid.addComponentColumn(fundHelpRequestDto -> {
            final Select<FundHelpRequestStatus> statuses = new Select<>();
            statuses.setItems(FundHelpRequestStatus.values());
            statuses.setItemLabelGenerator(status -> messageSource.getMessage(status.getLocalizedValue(), null, locale));
            statuses.setValue(fundHelpRequestDto.getStatus());
            statuses.addValueChangeListener(event -> {
                fundService.updateFundHelpRequestStatus(fundHelpRequestDto.getId(), event.getValue());
                UI.getCurrent().getPage().reload();
            });
            return statuses;
        }).setHeader(requestStatusHeader).setSortable(true);
        grid.addColumn(FundHelpRequestDto::getFormattedCreateAt).setHeader(requestCreatedAtHeader).setSortable(true);
        grid.addColumn(FundHelpRequestDto::getDescription).setHeader(requestDetailsHeader).setSortable(true);
        grid.addComponentColumn(fundHelpRequestDto -> this.executorLayout(fundHelpRequestDto.getExecutor()))
                .setHeader(requestExecutorHeader).setSortable(true);
        grid.setItems(dataProvider);

        this.add(grid);
    }

    private VerticalLayout executorLayout(final HelpRequestExecutorDto executor) {
        final String moreInfoDetails = messageSource.getMessage(LocalizationUtils.UI.CommonText.MORE_INFO_DETAILS_TEXT, null, locale);
        final String phoneInfoText = messageSource.getMessage(LocalizationUtils.UI.TeamView.PHONE_FIELD_TEXT, null, locale);
        final String emailInfoText = messageSource.getMessage(LocalizationUtils.UI.TeamView.EMAIL_FIELD_TEXT, null, locale);
        final String organizationNameText = messageSource.getMessage(LocalizationUtils.UI.VisitorView.ORGANIZATION_NAME_FILED_TEXT, null, locale);

        final HorizontalLayout memberLayout = new HorizontalLayout();
        memberLayout.setAlignItems(FlexComponent.Alignment.START);
        memberLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        final H4Header fullNameHeader = new H4Header(executor.getFullName());

        final Details memberDetails = new Details(moreInfoDetails);
        final String phone = Objects.isNull(executor.getPhone()) ? StringSymbolUtils.HYPHEN : executor.getPhone();
        final String email = Objects.isNull(executor.getEmail()) ? StringSymbolUtils.HYPHEN : executor.getEmail();
        final String organizationName = Objects.isNull(executor.getOrganizationName()) ? StringSymbolUtils.HYPHEN : executor.getOrganizationName();

        final Span phoneSpan = new Span(phoneInfoText + SPACE + phone);
        final Span emailSpan = new Span(emailInfoText + SPACE + email);
        final Span organizationSpan = new Span(organizationNameText + SPACE + organizationName);
        final VerticalLayout detailsContent = new VerticalLayout(organizationSpan, phoneSpan, emailSpan);
        detailsContent.setSpacing(false);
        detailsContent.setPadding(false);
        memberDetails.addContent(detailsContent);

        return new VerticalLayout(fullNameHeader, memberDetails);
    }
}
