package com.evolunteer.evm.ui.components.app.layout.view.cabinet;

import com.evolunteer.evm.ui.components.app.layout.ParentNavigationLayout;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = RouteUtils.HOME, layout = ParentNavigationLayout.class)
public class HomeView extends VerticalLayout {

    public HomeView() {

    }
}
