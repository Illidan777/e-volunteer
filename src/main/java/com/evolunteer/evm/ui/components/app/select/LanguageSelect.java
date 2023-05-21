package com.evolunteer.evm.ui.components.app.select;

import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.utils.CookieUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.select.Select;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.stream.Collectors;

public class LanguageSelect extends Select<Locale> {

    public LanguageSelect(MessageSource messageSource) {
        final Locale locale = LocalizationUtils.getLocale();
        final String placeholder = messageSource.getMessage(LocalizationUtils.UI.Language.CHOOSE_LANGUAGE_TEXT, null, locale);

        this.setPlaceholder(placeholder);
        this.setItems(LocalizationUtils.SUPPORTED_LOCALE_MAP.values().stream().map(LocalizationUtils.LocaleWrapper::getLocale).collect(Collectors.toSet()));
        this.setItemLabelGenerator(item -> messageSource.getMessage(LocalizationUtils.SUPPORTED_LOCALE_MAP.get(item.getLanguage()).getName(), null, locale));
        this.addValueChangeListener(valueChangeEvent -> {
            final Locale selectedValue = valueChangeEvent.getValue();
            CookieUtils.addCookie(CookieUtils.LANGUAGE_COOKIE_NAME, selectedValue.getLanguage());
            UI.getCurrent().getPage().reload();
        });
    }
}
