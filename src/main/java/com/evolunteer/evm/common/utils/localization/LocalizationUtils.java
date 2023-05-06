package com.evolunteer.evm.common.utils.localization;

import com.evolunteer.evm.ui.utils.CookieUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalizationUtils {

    public static final String ENGLISH_LANGUAGE = "en";
    public static final String UKRAINIAN_LANGUAGE = "ukr";

    public static final Map<String, Locale> SUPPORTED_LOCALE_MAP = Map.of(
            ENGLISH_LANGUAGE, new Locale(ENGLISH_LANGUAGE),
            UKRAINIAN_LANGUAGE, new Locale(UKRAINIAN_LANGUAGE)
    );

    public static Locale getLocale() {
        return CookieUtils.getCookieValue(CookieUtils.LANGUAGE_COOKIE_NAME)
                .map(cookie -> SUPPORTED_LOCALE_MAP.getOrDefault(cookie.getValue(), Locale.getDefault()))
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
        public static class RegistrationDialog {
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
            public static final String GO_BACK_TO_LOGIN_BUTTON_TEXT = "ui.registration.account.verification.go-back-to-login-button.text";
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
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class CommonText {
            public static final String CONFIRM_BUTTON_TEXT = "ui.common.confirm-button.text";
            public static final String CANCEL_BUTTON_TEXT = "ui.common.cancel-button.text";
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
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EmailNotification {
        public static final String ACCOUNT_VERIFICATION_NOTIFICATION_SUBJECT = "account.verification.subject";
        public static final String ACCOUNT_VERIFICATION_NOTIFICATION_PATTERN = "account.verification.subject.pattern";
        public static final String ACCOUNT_PASSWORD_RECOVER_NOTIFICATION_SUBJECT = "account.password-recover.subject";
        public static final String ACCOUNT_PASSWORD_RECOVER_NOTIFICATION_PATTERN = "account.password-recover.pattern";
    }
}
