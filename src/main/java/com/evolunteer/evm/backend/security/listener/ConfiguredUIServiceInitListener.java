package com.evolunteer.evm.backend.security.listener;

import com.evolunteer.evm.backend.security.utils.SecurityUtils;
import com.evolunteer.evm.ui.components.app.layout.view.login.AccountVerificationView;
import com.evolunteer.evm.ui.components.app.layout.view.intro.IntroductionView;
import com.evolunteer.evm.ui.components.app.layout.view.login.LoginView;
import com.evolunteer.evm.ui.components.app.layout.view.login.PasswordRecoverView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfiguredUIServiceInitListener implements VaadinServiceInitListener {

    private static final List<Class<?>> ALLOWED_NAVIGATION_TARGETS = List.of(
            LoginView.class,
            AccountVerificationView.class,
            IntroductionView.class,
            PasswordRecoverView.class);

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent event) {
        if(!ALLOWED_NAVIGATION_TARGETS.contains(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(IntroductionView.class);
        }
    }
}
