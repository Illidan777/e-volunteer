package com.evolunteer.evm.ui.components.app.view.cabinet;

import com.evolunteer.evm.ui.components.app.layout.navigation.ParentNavigationLayout;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = RouteUtils.STOCK_ROUTE, layout = ParentNavigationLayout.class)
public class StockView extends VerticalLayout {

    public StockView() {
    }
}
