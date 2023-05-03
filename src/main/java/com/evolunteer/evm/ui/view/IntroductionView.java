package com.evolunteer.evm.ui.view;

import com.evolunteer.evm.common.utils.LocalizationUtils;
import com.evolunteer.evm.ui.layout.LanguageLayout;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import org.springframework.context.MessageSource;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Locale;

@Route(RouteUtils.INTRO_ROUTE)
@PageTitle("Introduction | E-Volunteer")
public class IntroductionView extends VerticalLayout {

    public IntroductionView(MessageSource messageSource) {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        final String introductionText = messageSource.getMessage(LocalizationUtils.UI.IntroductionView.INTRODUCTION_TEXT, null, LocalizationUtils.getLocale());
        final String simpleClientButtonText = messageSource.getMessage(LocalizationUtils.UI.IntroductionView.SIMPLE_CLIENT_BUTTON_TEXT, null, LocalizationUtils.getLocale());
        final String volunteerClientButtonText = messageSource.getMessage(LocalizationUtils.UI.IntroductionView.VOLUNTEER_BUTTON_TEXT, null, LocalizationUtils.getLocale());

        final Div introductionDiv = new Div();
        introductionDiv.setWidth("100%");
        introductionDiv.getStyle().set("text-align", "center");
        introductionDiv.setText(introductionText);


        final Button volunteerButton = new Button(volunteerClientButtonText, buttonClickEvent -> UI.getCurrent().navigate(RouteUtils.LOGIN_ROUTE));
        final Button simpleClientButton = new Button(simpleClientButtonText, buttonClickEvent -> UI.getCurrent().navigate("ldsfsd"));

        final HorizontalLayout buttonLayout = new HorizontalLayout(volunteerButton, simpleClientButton);
        buttonLayout.setWidth("100%");
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        introductionDiv.add(buttonLayout);
        add(new LanguageLayout(), introductionDiv);
    }
}
