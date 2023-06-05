package com.evolunteer.evm.common.utils.localization;

import com.evolunteer.evm.ui.utils.CookieUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalizationUtils {

    public static final String ENGLISH_LANGUAGE = "en";
    public static final String UKRAINIAN_LANGUAGE = "ukr";

    public static final Map<String, LocaleWrapper> SUPPORTED_LOCALE_MAP = Map.of(
            ENGLISH_LANGUAGE, LocaleWrapper.of(UI.Language.ENGLISH_LANGUAGE_TEXT, new Locale(ENGLISH_LANGUAGE)),
            UKRAINIAN_LANGUAGE, LocaleWrapper.of(UI.Language.UKRAINIAN_LANGUAGE_TEXT, new Locale(UKRAINIAN_LANGUAGE))
    );

    public static Locale getLocale() {
        return CookieUtils.getCookieValue(CookieUtils.LANGUAGE_COOKIE_NAME)
                .map(cookie -> SUPPORTED_LOCALE_MAP.getOrDefault(cookie.getValue(),
                        LocaleWrapper.of(UI.Language.ENGLISH_LANGUAGE_TEXT, Locale.getDefault())).getLocale())
                .orElse(Locale.getDefault());
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UI {

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class IntroductionView {
            public static final String INTRODUCTION_TEXT = "ui.introduction.introduction-div.text";
            public static final String SIMPLE_CLIENT_BUTTON_TEXT = "ui.introduction.introduction-div.simple-client-button.text";
            public static final String VOLUNTEER_BUTTON_TEXT = "ui.introduction.introduction-div.volunteer-button.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class LoginView {
            public static final String HEADER_TEXT = "ui.login.header.text";
            public static final String TITLE = "ui.login.title.text";
            public static final String USERNAME_FIELD_TEXT = "ui.login.username-field.text";
            public static final String PASSWORD_FIELD_TEXT = "ui.login.password-field.text";
            public static final String SUBMIT_BUTTON_TEXT = "ui.login.submit-button.text";
            public static final String FORGOT_PASSWORD_BUTTON_TEXT = "ui.login.forgot-password-button.text";
            public static final String ERROR_TITLE_TEXT = "ui.login.error-title.text";
            public static final String ERROR_MESSAGE_TEXT = "ui.login.error-message.text";
            public static final String REGISTRATION_BUTTON_TEXT = "ui.login.registration-button.text";
            public static final String GOOGLE_LOGIN_BUTTON_TEXT = "ui.login.google-login-button.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class UserRegistrationDialog {
            public static final String HEADER_TEXT = "ui.registration.header.text";
            public static final String CREDENTIALS_HEADER_TEXT = "ui.registration.credentials-header.text";
            public static final String NAME_FIELD_TEXT = "ui.registration.name-field.text";
            public static final String SURNAME_FIELD_TEXT = "ui.registration.surname-field.text";
            public static final String MIDDLE_NAME_FIELD_TEXT = "ui.registration.middle-name-field.text";
            public static final String PHONE_FIELD_TEXT = "ui.registration.phone-field.text";
            public static final String EMAIL_FIELD_TEXT = "ui.registration.email-field.text";
            public static final String BIRTHDATE_FIELD_TEXT = "ui.registration.birthdate-field.text";
            public static final String USERNAME_FIELD_TEXT = "ui.registration.username-field.text";
            public static final String PASSWORD_FIELD_TEXT = "ui.registration.password-field.text";
            public static final String SUCCESS_REGISTRATION_TEXT = "ui.registration.success.text";
            public static final String SUCCESS_ACCOUNT_VERIFICATION = "ui.registration.account.verification.success.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class FundRegistrationDialog {
            public static final String HEADER_TEXT = "ui.registration.fund.header.text";
            public static final String MAIN_INFO_HEADER_TEXT = "ui.registration.fund.main-info.header.text";
            public static final String COMMUNICATIONS_HEADER_TEXT = "ui.registration.fund.communication.header.text";
            public static final String NAME_FIELD_TEXT = "ui.registration.fund.name-field.text";
            public static final String PHONE_FIELD_TEXT = UserRegistrationDialog.PHONE_FIELD_TEXT;
            public static final String EMAIL_FIELD_TEXT = UserRegistrationDialog.EMAIL_FIELD_TEXT;
            public static final String DESCRIPTION_FIELD_TEXT = "ui.registration.fund.description-field.text";
            public static final String CATEGORY_FIELD_TEXT = "ui.registration.fund.category-field.text";
            public static final String CATEGORY_MEDICINE_TEXT = "ui.registration.fund.category-medicine.text";
            public static final String CATEGORY_PRODUCT_TEXT = "ui.registration.fund.category-product.text";
            public static final String CATEGORY_MILITARY_STAFF_TEXT = "ui.registration.fund.category-military-staff.text";
            public static final String CATEGORY_CLOTH_TEXT = "ui.registration.fund.category-cloth.text";
            public static final String CATEGORY_HYGIENE_TEXT = "ui.registration.fund.category-hygiene.text";
            public static final String REQUISITES_HEADER_TEXT = "ui.registration.fund.header.requisites.text";
            public static final String REQUISITE_RECIPIENT_FIELD_TEXT = "ui.registration.fund.requisite.recipient-field.text";
            public static final String REQUISITE_BANK_FIELD_TEXT = "ui.registration.fund.requisite.bank-field.text";
            public static final String REQUISITE_BANK_CODE_FIELD_TEXT = "ui.registration.fund.requisite.bank-code-field.text";
            public static final String REQUISITE_IBAN_FIELD_TEXT = "ui.registration.fund.requisite.iban-field.text";
            public static final String REQUISITE_PAYMENT_ACCOUNT_FIELD_TEXT = "ui.registration.fund.requisite.payment-account-field.text";
            public static final String REQUISITE_SWIFT_CODE_FIELD_TEXT = "ui.registration.fund.requisite.swift-code-field.text";
            public static final String REQUISITE_LEGAL_ADDRESS_FIELD_TEXT = "ui.registration.fund.requisite.legal-address-field.text";
            public static final String REQUISITE_PAYMENT_LINK_FIELD_TEXT = "ui.registration.fund.requisite.payment-link-field.text";
            public static final String SUCCESSFULLY_FUND_REGISTRATION = "ui.registration.fund.success.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Address {
            public static final String HEADER_TEXT = "ui.address.header.text";
            public static final String COUNTRY_FIELD_TEXT = "ui.address.country-field.text";
            public static final String REGION_FIELD_TEXT = "ui.address.region-field.text";
            public static final String CITY_FIELD_TEXT = "ui.address.city-field.text";
            public static final String STREET_FIELD_TEXT = "ui.address.street-field.text";
            public static final String HOUSE_FIELD_TEXT = "ui.address.house-field.text";
            public static final String CORPUS_FIELD_TEXT = "ui.address.corpus-field.text";
            public static final String OFFICE_FIELD_TEXT = "ui.address.office-field.text";
            public static final String POST_INDEX_FIELD_TEXT = "ui.address.post-index-field.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class PasswordRecoverDialog {
            public static final String HEADER_TEXT = "ui.password-recover.header.text";
            public static final String USERNAME_FIELD_TEXT = "ui.password-recover.username-field.text";
            public static final String EMAIL_CONFIRMATION_TEXT = "ui.password-recover.email-confirmation.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class PasswordRecoverView {
            public static final String HEADER_TEXT = PasswordRecoverDialog.HEADER_TEXT;
            public static final String NEW_PASSWORD_FIELD_TEXT = "ui.password-recover.new-password-field.text";
            public static final String NEW_PASSWORD_CONFIRMING_FIELD_TEXT = "ui.password-recover.new-password-confirming-field.text";
            public static final String PASSWORD_SUCCESSFULLY_UPDATED = "ui.password-recover.password.successfully-updated.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class HomeView {
            public static final String HEADER_TEXT = "ui.home-view.header.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class UserProfileView {
            public static final String PERSONAL_DATA_HEADER_TEXT = "ui.user-profile.personal-data.header.text";
            public static final String PROFILE_PICTURE_HEADER_TEXT = "ui.user-profile.profile-picture.header.text";
            public static final String NAME_FIELD_TEXT = UserRegistrationDialog.NAME_FIELD_TEXT;
            public static final String SURNAME_FIELD_TEXT = UserRegistrationDialog.SURNAME_FIELD_TEXT;
            public static final String MIDDLE_NAME_FIELD_TEXT = UserRegistrationDialog.MIDDLE_NAME_FIELD_TEXT;
            public static final String PHONE_FIELD_TEXT = UserRegistrationDialog.PHONE_FIELD_TEXT;
            public static final String EMAIL_FIELD_TEXT = UserRegistrationDialog.EMAIL_FIELD_TEXT;
            public static final String BIRTHDATE_FIELD_TEXT = UserRegistrationDialog.BIRTHDATE_FIELD_TEXT;
            public static final String PERSONAL_DATA_SUCCESSFULLY_UPDATED = "ui.user-profile.personal-data.successfully-updated.text";
            public static final String PICTURE_SUCCESSFULLY_UPDATED = "ui.user-profile.picture.successfully-updated.text";
            public static final String PICTURE_SUCCESSFULLY_DELETED = "ui.user-profile.picture.successfully-deleted.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class FundProfileView {
            public static final String NO_FUND_DETECTED_HEADER_TEXT = "ui.fund-profile.no-fund-detected.header.text";
            public static final String CREATE_FUND_BUTTON_TEXT = "ui.fund-profile.create-fund.button.text";
            public static final String APPLY_FOR_PARTICIPATION_HEADER_TEXT = "ui.fund-profile.apply-for-participation.header.text";
            public static final String APPLY_FOR_PARTICIPATION_DESCRIPTION_HEADER_TEXT = "ui.fund-profile.apply-for-participation.text";
            public static final String SUCCESSFULLY_FUND_UPDATED = "ui.fund-profile.update.success.text";
            public static final String HEADER_TEXT = "ui.fund-profile.fund.header.text";
            public static final String INVITATIONS_HEADER_TEXT = "ui.fund-profile.invitations.header.text";
            public static final String NO_INVITATIONS_TEXT = "ui.fund-profile.no-invitations.text";
            public static final String PHONE_FIELD_TEXT = UserRegistrationDialog.PHONE_FIELD_TEXT;
            public static final String EMAIL_FIELD_TEXT = UserRegistrationDialog.EMAIL_FIELD_TEXT;
            public static final String DESCRIPTION_FIELD_TEXT = FundRegistrationDialog.DESCRIPTION_FIELD_TEXT;
            public static final String FUND_SELECT_TEXT = "ui.fund-profile.search-fund.text";
            public static final String NO_FUND_SELECTED_TO_APPLY_TEXT = "ui.fund-profile.fund-select.no-fund-selected.text";
            public static final String APPLICATION_SUCCESSFULLY_SENT_TEXT = "ui.fund-profile.application-successfully-sent.text";
            public static final String APPLICATION_IN_PROGRESS_TEXT = "ui.fund-profile.application-in-progress.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class TeamView {
            public static final String PHONE_FIELD_TEXT = UserRegistrationDialog.PHONE_FIELD_TEXT;
            public static final String EMAIL_FIELD_TEXT = UserRegistrationDialog.EMAIL_FIELD_TEXT;
            public static final String INVITE_MEMBER_HEADER_TEXT = "ui.team.invite-member.header.text";
            public static final String INVITE_MEMBER_DESCRIPTION_TEXT = "ui.team.invite-member-description.header.text";
            public static final String APPLICATIONS_HEADER_TEXT = "ui.team.applications.header.text";
            public static final String USERS_SELECT_TEXT = "ui.team.search-users.header.text";
            public static final String INVITE_MEMBER_BUTTON_TEXT = "ui.team.invite-member.button.text";
            public static final String NO_APPLICATIONS_TEXT = "ui.team.no-applications.text";
            public static final String NO_USER_SELECTED_TO_INVITE_TEXT = "ui.team.users-select.no-user-selected.text";
            public static final String INVITATION_SUCCESSFULLY_SENT_TEXT = "ui.team.invitation-successfully-sent.text";
            public static final String INVITATION_IN_PROGRESS_TEXT = "ui.team.invitation-in-progress.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Language {
            public static final String ENGLISH_LANGUAGE_TEXT = "ui.language.english.text";
            public static final String UKRAINIAN_LANGUAGE_TEXT = "ui.language.ukrainian.text";
            public static final String CHOOSE_LANGUAGE_TEXT = "ui.language.choose-language-header";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class CommonText {
            public static final String CONFIRM_BUTTON_TEXT = "ui.common.confirm-button.text";
            public static final String CANCEL_BUTTON_TEXT = "ui.common.cancel-button.text";
            public static final String SAVE_BUTTON_TEXT = "ui.common.save-button.text";
            public static final String DELETE_BUTTON_TEXT = "ui.common.delete-button.text";
            public static final String ACCEPT_BUTTON_TEXT = "ui.common.accept-button.text";
            public static final String REJECT_BUTTON_TEXT = "ui.common.reject-button.text";
            public static final String MORE_INFO_DETAILS_TEXT = "ui.common.more-info.details.text";
            public static final String SEND_BUTTON_TEXT = "ui.common.send-button.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class VerificationLinkLayout {
            public static final String GO_BACK_TO_LOGIN_BUTTON_TEXT = "ui.verification-layout.go-back-to-login-button.text";
            public static final String GET_NEW_LINK_BUTTON_TEXT = "ui.verification-layout.get-new-link-button.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class NavigationLayout {
            public static final String HEADER_TEXT = "ui.navigation-layout.header.text";
            public static final String ITEM_MY_PROFILE_TEXT = "ui.navigation-layout.item.my-profile.header.text";
            public static final String ITEM_HOME_TEXT = "ui.navigation-layout.item.home.header.text";
            public static final String ITEM_FUND_PROFILE_TEXT = "ui.navigation-layout.item.fund-profile.header.text";
            public static final String ITEM_TEAM_TEXT = "ui.navigation-layout.item.team.header.text";
            public static final String ITEM_STOCK_TEXT = "ui.navigation-layout.item.stock.header.text";
            public static final String LOG_OUT_BUTTON_TEXT = "ui.navigation-layout.log-out.button.text";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class FileUpload {
            public static final String SUCCESS = "ui.file-upload.success";
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Error {
        public static final String VALIDATION_USER_NAME_ERROR = "error.validation.required.name";
        public static final String VALIDATION_SURNAME_ERROR = "error.validation.required.surname";
        public static final String VALIDATION_PHONE_ERROR = "error.validation.required.phone";
        public static final String VALIDATION_EMAIL_ERROR = "error.validation.required-and-pattern.email";
        public static final String VALIDATION_BIRTHDATE_ERROR = "error.validation.required.birthdate";
        public static final String VALIDATION_USERNAME_ERROR = "error.validation.required.username";
        public static final String VALIDATION_PASSWORD_ERROR = "error.validation.required-and-pattern.password";
        public static final String VALIDATION_PASSWORD_CONFIRMING_ERROR = "error.validation.password.confirming";
        public static final String VALIDATION_ACCOUNT_ALREADY_EXIST_BY_USERNAME_ERROR = "error.validation.exists.account-by-username";
        public static final String VALIDATION_ACCOUNT_DOES_NOT_EXIST_BY_USERNAME_ERROR = "error.validation.not-exists.account-by-username";
        public static final String VALIDATION_ACCOUNT_IS_NOT_VERIFIED_ERROR = "error.validation.account.not-verified";
        public static final String VALIDATION_INVALID_LINK_ERROR = "error.validation.invalid.link";
        public static final String VALIDATION_EXPIRED_VERIFICATION_LINK_ERROR = "error.validation.expired.verification.link";
        public static final String AUTHORIZATION_ERROR = "error.authorization";
        public static final String VALIDATION_FILE_EMPTY_FILENAME_ERROR = "error.validation.empty-filename.file";
        public static final String VALIDATION_FILE_EMPTY_FILE_EXTENSION_ERROR = "error.validation.empty-file-extension.file";
        public static final String VALIDATION_FILE_ERROR = "error.validation.file";
        public static final String VALIDATION_FILE_SIZE_ERROR = "error.uploading.size.file";
        public static final String UPLOADING_FILE_ERROR = "error.uploading.file";
        public static final String VALIDATION_USER_PROFILE_PICTURE_EXTENSION_ERROR = "error.validation.user-profile-picture";
        public static final String VALIDATION_COUNTRY_ERROR = "error.validation.required.country";
        public static final String VALIDATION_REGION_ERROR = "error.validation.required.region";
        public static final String VALIDATION_CITY_ERROR = "error.validation.required.city";
        public static final String VALIDATION_STREET_ERROR = "error.validation.required.street";
        public static final String VALIDATION_HOUSE_ERROR = "error.validation.required.house";
        public static final String VALIDATION_FUND_NAME_ERROR = "error.validation.required.fund-name";
        public static final String VALIDATION_FUND_DESCRIPTION_ERROR = "error.validation.required.fund-description";
        public static final String VALIDATION_FUND_CATEGORIES_ERROR = "error.validation.required.fund-categories";
        public static final String VALIDATION_REQUISITE_RECIPIENT_ERROR = "error.validation.required.requisite-recipient";
        public static final String VALIDATION_REQUISITE_BANK_ERROR = "error.validation.required.requisite-bank";
        public static final String VALIDATION_REQUISITE_BANK_CODE_ERROR = "error.validation.required.requisite-bank-code";
        public static final String VALIDATION_REQUISITE_PAYMENT_ACCOUNT_ERROR = "error.validation.required.requisite-payment-account";
        public static final String VALIDATION_REQUISITE_LEGAL_ADDRESS_ERROR = "error.validation.required.requisite-legal-address";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EmailNotification {
        public static final String ACCOUNT_VERIFICATION_NOTIFICATION_SUBJECT = "account.verification.subject";
        public static final String ACCOUNT_VERIFICATION_NOTIFICATION_PATTERN = "account.verification.subject.pattern";
        public static final String ACCOUNT_PASSWORD_RECOVER_NOTIFICATION_SUBJECT = "account.password-recover.subject";
        public static final String ACCOUNT_PASSWORD_RECOVER_NOTIFICATION_PATTERN = "account.password-recover.pattern";
        public static final String USER_FUND_INVITATION_NOTIFICATION_SUBJECT = "user.fund.invitation.subject";
        public static final String USER_FUND_INVITATION_NOTIFICATION_PATTERN = "user.fund.invitation.pattern";
        public static final String SUCCESSFULLY_ACCEPTED_MEMBER_OF_FUND_SUBJECT = "user.fund.request-accepted.subject";
        public static final String SUCCESSFULLY_ACCEPTED_MEMBER_OF_FUND_PATTERN = "user.fund.request-accepted.pattern";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class LocaleWrapper {
        private String name;
        private Locale locale;
    }
}
