package com.evolunteer.evm.ui.components.general.notification;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY_INLINE;

public class NotificationFactory {

    private static final Notification.Position DEFAULT_NOTIFICATION_POSITION = Notification.Position.BOTTOM_END;
    private static final Integer DEFAULT_NOTIFICATION_DURATION = 5000;

    public static Notification success(final String message) {
        final Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        final Icon icon = VaadinIcon.CHECK_CIRCLE.create();

        notification.add(createTextLayout(message, notification, icon));
        notification.setPosition(DEFAULT_NOTIFICATION_POSITION);
        notification.setDuration(DEFAULT_NOTIFICATION_DURATION);

        return notification;
    }

    public static Notification error(final String message) {
        final Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        final Icon icon = VaadinIcon.WARNING.create();

        notification.add(createTextLayout(message, notification, icon));
        notification.setPosition(DEFAULT_NOTIFICATION_POSITION);
        notification.setDuration(DEFAULT_NOTIFICATION_DURATION);

        return notification;
    }

    private static HorizontalLayout createTextLayout(final String message, final Notification notification, final Icon icon) {
        final Div info = new Div(new Text(message));
        final HorizontalLayout layout = new HorizontalLayout(icon, info, createCloseBtn(notification));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        return layout;
    }

    public static Button createCloseBtn(final Notification notification) {
        final Button closeBtn = new Button(VaadinIcon.CLOSE_BIG.create(),
                clickEvent -> notification.close());
        closeBtn.addThemeVariants(LUMO_TERTIARY_INLINE);
        return closeBtn;
    }
}
