package com.evolunteer.evm.ui.components.app.view.cabinet;

import com.evolunteer.evm.backend.service.fund_management.FundService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.user_management.UserDto;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.dialog.fund_management.FundRegistrationDialog;
import com.evolunteer.evm.ui.components.app.layout.navigation.ParentNavigationLayout;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundProfileView.*;

@Route(value = RouteUtils.FUND_PROFILE_ROUTE, layout = ParentNavigationLayout.class)
public class FundProfileView extends VerticalLayout {

    private final Locale locale;
    private final MessageSource messageSource;
    private final FundService fundService;
    private final UserDto contextUser;

    public FundProfileView(MessageSource messageSource, UserService userService, FundService fundService) {
        this.locale = LocalizationUtils.getLocale();
        this.messageSource = messageSource;
        this.fundService = fundService;
        this.contextUser = userService.getContextUser();

        if(Objects.isNull(contextUser.getFund())) {
            this.createIntroduction();
        } else {
            NotificationFactory.success("On development!").open();
        }
    }

    private void createIntroduction() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        final String createFundButtonText = messageSource.getMessage(CREATE_FUND_BUTTON_TEXT, null, locale);
        final String applyForParticipationButtonText = messageSource.getMessage(APPLY_FOR_PARTICIPATION_BUTTON_TEXT, null, locale);
        final String checkInvitationButtonText = messageSource.getMessage(CHECK_INVITATIONS_BUTTON_TEXT, null, locale);

        final H3Header introductionHeader = new H3Header(messageSource, locale, NO_FUND_DETECTED_HEADER_TEXT);
        final Button createFundButton = new Button(createFundButtonText, event -> new FundRegistrationDialog(messageSource, locale, fundService).open());
        final Button applyForParticipationdButton = new Button(applyForParticipationButtonText, event -> NotificationFactory.success("On development!").open());
        final Button checkInvitationsButton = new Button(checkInvitationButtonText, event -> NotificationFactory.success("On development!").open());
        final HorizontalLayout buttonLayout = new HorizontalLayout(createFundButton, applyForParticipationdButton, checkInvitationsButton);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        final Div introductionDiv = new Div(introductionHeader, buttonLayout);
        introductionDiv.setWidth("100%");
        introductionDiv.getStyle().set("text-align", "center");

        this.add(introductionDiv);
    }
}
