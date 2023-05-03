package com.evolunteer.evm.ui.view;

import com.evolunteer.evm.backend.service.UserService;
import com.evolunteer.evm.common.utils.LocalizationUtils;
import com.evolunteer.evm.ui.dialog.RegistrationDialog;
import com.evolunteer.evm.ui.layout.LanguageLayout;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Route(RouteUtils.LOGIN_ROUTE)
@PageTitle("Login | E-Volunteer")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    public LoginView(MessageSource messageSource, UserService userService){
        final Locale locale = LocalizationUtils.getLocale();
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        final String loginHeader = messageSource.getMessage(LocalizationUtils.UI.LoginView.HEADER_TEXT, null, locale);
        final String loginTitle = messageSource.getMessage(LocalizationUtils.UI.LoginView.TITLE, null, locale);
        final String loginUsername = messageSource.getMessage(LocalizationUtils.UI.LoginView.USERNAME_FIELD_TEXT, null, locale);
        final String loginPassword = messageSource.getMessage(LocalizationUtils.UI.LoginView.PASSWORD_FIELD_TEXT, null, locale);
        final String loginSubmit = messageSource.getMessage(LocalizationUtils.UI.LoginView.SUBMIT_BUTTON_TEXT, null, locale);
        final String loginForgotPassword = messageSource.getMessage(LocalizationUtils.UI.LoginView.FORGOT_PASSWORD_BUTTON_TEXT, null, locale);
        final String loginErrorTitle = messageSource.getMessage(LocalizationUtils.UI.LoginView.ERROR_TITLE_TEXT, null, locale);
        final String loginErrorMessage = messageSource.getMessage(LocalizationUtils.UI.LoginView.ERROR_MESSAGE_TEXT, null, locale);
        final String loginRegistration = messageSource.getMessage(LocalizationUtils.UI.LoginView.REGISTRATION_BUTTON_TEXT, null, locale);
        final String loginWithGoogle = messageSource.getMessage(LocalizationUtils.UI.LoginView.GOOGLE_LOGIN_BUTTON_TEXT, null, locale);

        final LoginI18n i18n = LoginI18n.createDefault();

        final LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle(loginTitle);
        i18nForm.setUsername(loginUsername);
        i18nForm.setPassword(loginPassword);
        i18nForm.setSubmit(loginSubmit);
        i18nForm.setForgotPassword(loginForgotPassword);
        i18n.setForm(i18nForm);

        final LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle(loginErrorTitle);
        i18nErrorMessage.setMessage(loginErrorMessage);
        i18n.setErrorMessage(i18nErrorMessage);

        final LoginForm loginForm = new LoginForm();
        loginForm.setI18n(i18n);

        login.setAction("login");

        final H1 logInHeader = new H1(loginHeader);
        final Button registrationButton = new Button(loginRegistration, buttonClickEvent -> new RegistrationDialog(messageSource, locale, userService).open());
        final Button googleButton = new Button(loginWithGoogle, VaadinIcon.GOOGLE_PLUS_SQUARE.create(), buttonClickEvent -> UI.getCurrent().getPage().setLocation("/oauth2/authorization/google"));

        add(
                new LanguageLayout(),
                logInHeader,
                login,
                registrationButton, googleButton
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
