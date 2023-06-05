package com.evolunteer.evm.ui.components.app.layout.navigation;

import com.evolunteer.evm.backend.service.file_management.FileService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.general.Pair;
import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.app.view.cabinet.*;
import com.evolunteer.evm.ui.components.general.header.H1Header;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.app.select.LanguageSelect;
import com.evolunteer.evm.ui.components.general.image.ImageAvatar;
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

import java.util.*;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.NavigationLayout.LOG_OUT_BUTTON_TEXT;

public class ParentNavigationLayout extends AppLayout {

    private final Locale locale;
    private final MessageSource messageSource;
    private final FileService fileService;
    private final BaseUserDto contextUser;

    public ParentNavigationLayout(MessageSource messageSource, FileService fileService, UserService userService) {
        this.locale = LocalizationUtils.getLocale();
        this.messageSource = messageSource;
        this.fileService = fileService;
        this.contextUser = userService.getContextUser();

        final Tab homeTab = new Tab(
                new Icon(VaadinIcon.HOME),
                new H1Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.ITEM_HOME_TEXT));
        final Tab myProfileTab = new Tab(
                new Icon(VaadinIcon.USER),
                new H1Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.ITEM_MY_PROFILE_TEXT));
        final Tab fundProfileTab = new Tab(
                new Icon(VaadinIcon.BRIEFCASE),
                new H1Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.ITEM_FUND_PROFILE_TEXT));
        final Tab teamTab = new Tab(
                new Icon(VaadinIcon.USERS),
                new H1Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.ITEM_TEAM_TEXT));
        final Tab stockTab = new Tab(
                new Icon(VaadinIcon.PACKAGE),
                new H1Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.ITEM_STOCK_TEXT));
        stockTab.setEnabled(false);

        final Tabs tabs;
        if (Objects.isNull(contextUser.getFund())) {
            tabs = this.createTabs(
                    Pair.of(homeTab, HomeView.class),
                    Pair.of(myProfileTab, UserProfileView.class),
                    Pair.of(fundProfileTab, FundProfileView.class)
            );
        }else {
            tabs = this.createTabs(
                    Pair.of(homeTab, HomeView.class),
                    Pair.of(myProfileTab, UserProfileView.class),
                    Pair.of(fundProfileTab, FundProfileView.class),
                    Pair.of(teamTab, TeamView.class),
                    Pair.of(stockTab, StockView.class)
            );
        }

        this.addToDrawer(tabs);
        this.addToNavbar(
                new DrawerToggle(),
                new H3Header(messageSource, locale, LocalizationUtils.UI.NavigationLayout.HEADER_TEXT),
                this.createNavbarElementsLayout());
    }

    @SafeVarargs
    private Tabs createTabs(final Pair<Tab, Class<? extends Component>> ... tabSet) {
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

        final FileMetaDataDto userPicture = contextUser.getPicture();
        String pictureFileCode = null;
        if(Objects.nonNull(userPicture)) {
            pictureFileCode = userPicture.getCode();
        }
        final String username = contextUser.getAccountDetails().getUsername();
        final ImageAvatar userAvatar = new ImageAvatar(fileService, pictureFileCode, username);
        final H3Header usernameHeader = new H3Header(username);
        final HorizontalLayout loggedInUser = new HorizontalLayout(userAvatar, usernameHeader);
        loggedInUser.setAlignItems(FlexComponent.Alignment.CENTER);
        loggedInUser.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        final Div navbarElementsContainer = new Div(loggedInUser, languageSelectElement, logOutButtonElement);
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
