package com.evolunteer.evm.ui.components.app.view.cabinet;

import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.layout.navigation.ParentNavigationLayout;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Route(value = RouteUtils.HOME_ROUTE, layout = ParentNavigationLayout.class)
public class HomeView extends VerticalLayout {

    public HomeView(MessageSource messageSource) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        final Locale locale = LocalizationUtils.getLocale();

        final Div introductionDiv = new Div(new H3Header(messageSource, locale, LocalizationUtils.UI.HomeView.HEADER_TEXT));
        introductionDiv.setWidth("100%");
        introductionDiv.getStyle().set("text-align", "center");

        final Icon icon = new Icon(VaadinIcon.USER_HEART);

        this.add(introductionDiv, icon);
    }
}
