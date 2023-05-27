package com.evolunteer.evm.common.domain.enums.fund_management;

import lombok.Getter;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.FundRegistrationDialog.*;

@Getter
public enum FundActivityCategory {
    MEDICINE(CATEGORY_MEDICINE_TEXT),
    MILITARY_STAFF(CATEGORY_MILITARY_STAFF_TEXT),
    CLOTH(CATEGORY_CLOTH_TEXT),
    PRODUCT(CATEGORY_PRODUCT_TEXT),
    HYGIENE(CATEGORY_HYGIENE_TEXT);

    private final String localizedValue;

    FundActivityCategory(String localizedValue) {
        this.localizedValue = localizedValue;
    }
}
