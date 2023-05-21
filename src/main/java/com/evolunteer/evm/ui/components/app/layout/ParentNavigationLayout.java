package com.evolunteer.evm.ui.components.app.layout;

import com.evolunteer.evm.common.domain.dto.general.Pair;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.layout.view.cabinet.UserProfileView;
import com.evolunteer.evm.ui.components.general.header.H1Header;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.app.select.LanguageSelect;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.NavigationLayout.LOG_OUT_BUTTON_TEXT;

public class ParentNavigationLayout extends AppLayout {

    private final Locale locale;
    private final MessageSource messageSource;

    public ParentNavigationLayout(MessageSource messageSource) {
        this.locale = LocalizationUtils.getLocale();
        this.messageSource = messageSource;

        final Tab myProfileTab = new Tab(
                new Icon(VaadinIcon.USER),
                new H1Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.ITEM_MY_PROFILE_TEXT));

        final Tabs tabs = this.createTabs(
                Pair.of(myProfileTab, UserProfileView.class)
        );

        this.addToDrawer(tabs);
        this.addToNavbar(
                new DrawerToggle(),
                new H3Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.HEADER_TEXT),
                this.createNavbarElementsLayout());
    }

    @SafeVarargs
    private <T extends Component> Tabs createTabs(final Pair<Tab, Class<T>> ... tabSet) {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        final Map<Tab, Class<? extends Component>> tabViews = new HashMap<>();
        Arrays.asList(tabSet).forEach(tabViewPair -> {
            tabViews.put(tabViewPair.getKey(), tabViewPair.getValue());
            tabs.add(tabViewPair.getKey());
        });
        tabs.addSelectedChangeListener(selectedChangeEvent -> UI.getCurrent().navigate(tabViews.get(selectedChangeEvent.getSelectedTab())));
        return tabs;
    }

    private HorizontalLayout createNavbarElementsLayout() {
        final String logOutButtonText = messageSource.getMessage(LOG_OUT_BUTTON_TEXT, null, locale);

        final Button logOutButtonElement = new Button(logOutButtonText, new Icon(VaadinIcon.EXIT),
                buttonClickEvent -> UI.getCurrent().getPage().setLocation(RouteUtils.LOGOUT_ROUTE));
        logOutButtonElement.setIconAfterText(true);
        final LanguageSelect languageSelectElement = new LanguageSelect(messageSource);

        final Div navbarElementsContainer = new Div(languageSelectElement, logOutButtonElement);
        navbarElementsContainer.setWidth("100%");
        navbarElementsContainer.getStyle().set("text-align", "center");

        final HorizontalLayout navbar = new HorizontalLayout(navbarElementsContainer);
        navbar.setPadding(true);
        navbar.setSpacing(true);
        navbar.getStyle().set("margin-left", "auto");
        navbar.setAlignItems(FlexComponent.Alignment.END);
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        return navbar;
    }
}
