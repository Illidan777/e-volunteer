package com.evolunteer.evm.ui.components.app.view.cabinet;

import com.evolunteer.evm.backend.service.file_management.FileService;
import com.evolunteer.evm.backend.service.file_management.validator.impl.UserProfilePictureValidator;
import com.evolunteer.evm.backend.service.user_management.AccountService;
import com.evolunteer.evm.backend.service.user_management.UserService;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.user_management.BaseUserDto;
import com.evolunteer.evm.common.domain.request.user_management.UpdateUserRequest;
import com.evolunteer.evm.common.mapper.user_management.UserMapper;
import com.evolunteer.evm.common.utils.date.DateUtils;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.validation.ValidationUtils;
import com.evolunteer.evm.ui.components.app.div.PasswordRecoverDiv;
import com.evolunteer.evm.ui.components.app.layout.navigation.ParentNavigationLayout;
import com.evolunteer.evm.ui.components.general.button.DeleteButton;
import com.evolunteer.evm.ui.components.general.button.SaveButton;
import com.evolunteer.evm.ui.components.general.header.H3Header;
import com.evolunteer.evm.ui.components.general.layout.ImageFlexLayout;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.evolunteer.evm.ui.components.general.upload.SingleFileUploader;
import com.evolunteer.evm.ui.utils.RouteUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.evolunteer.evm.common.utils.localization.LocalizationUtils.UI.UserProfileView.*;

@Route(value = RouteUtils.USER_PROFILE_ROUTE, layout = ParentNavigationLayout.class)
public class UserProfileView extends VerticalLayout {

    private static final String USER_PROFILE_PICTURE_ALT = "my profile picture";
    private final Locale locale;
    private final MessageSource messageSource;
    private final Binder<UpdateUserRequest> updateUserBinder;
    private final FileService fileService;
    private final UserService userService;
    private final UserProfilePictureValidator userProfilePictureValidator;
    private final BaseUserDto contextUser;
    private final UpdateUserRequest updateUserRequest;

    public UserProfileView(MessageSource messageSource,
                           UserService userService,
                           AccountService accountService,
                           UserMapper userMapper,
                           FileService fileService,
                           UserProfilePictureValidator userProfilePictureValidator) {
        this.locale = LocalizationUtils.getLocale();
        this.messageSource = messageSource;
        this.updateUserBinder = new Binder<>();
        this.fileService = fileService;
        this.userService = userService;
        this.userProfilePictureValidator = userProfilePictureValidator;
        this.contextUser = userService.getContextUser();
        this.updateUserRequest = userMapper.mapUserDtoToUpdateUserRequest(contextUser);

        add(
                this.createProfilePictureDiv(),
                new Hr(),
                this.createPersonalDataDiv(),
                new Hr(),
                new PasswordRecoverDiv(messageSource, locale, accountService, contextUser.getAccountDetails().getId(), false)

        );
        updateUserBinder.readBean(updateUserRequest);
    }

    private Div createProfilePictureDiv() {
        final H3Header profilePictureHeader = new H3Header(messageSource, locale, LocalizationUtils.UI.UserProfileView.PROFILE_PICTURE_HEADER_TEXT);
        final SaveButton saveButton = new SaveButton(messageSource, locale);
        final DeleteButton deleteButton = new DeleteButton(messageSource, locale);
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setAlignItems(Alignment.START);
        buttonLayout.setJustifyContentMode(JustifyContentMode.START);

        final Div profilePicturDiv = new Div(profilePictureHeader);
        final SingleFileUploader pictureUploader = new SingleFileUploader(messageSource, locale, fileService, userProfilePictureValidator);
        final String userPictureFileCode;
        final boolean isPicturePresent = Objects.nonNull(contextUser.getPicture());
        if (isPicturePresent) {
            userPictureFileCode = contextUser.getPicture().getCode();
            if (pictureUploader.isAttached()) {
                this.remove(pictureUploader);
            }
            if(saveButton.isAttached()) {
                this.remove(saveButton);
            }
            if(!deleteButton.isAttached()) {
                buttonLayout.add(deleteButton);
            }
        } else {
            if (!pictureUploader.isAttached()) {
                profilePicturDiv.add(pictureUploader);
            }
            if(!saveButton.isAttached()) {
                buttonLayout.add(saveButton);
            }
            if(deleteButton.isAttached()) {
                buttonLayout.remove(deleteButton);
            }
            userPictureFileCode = null;
        }

        final ImageFlexLayout userProfilePicture = new ImageFlexLayout(fileService, userPictureFileCode, USER_PROFILE_PICTURE_ALT);

        final AtomicReference<Optional<FileMetaDataDto>> atomicUploadedPicture = new AtomicReference<>(Optional.empty());
        pictureUploader.addSucceededListener(event -> {
            atomicUploadedPicture.set(pictureUploader.handleSucceededUpload(event));
        });
        saveButton.addClickListener(this.updateUserPicture(atomicUploadedPicture));
        deleteButton.addClickListener(this.deleteUserPicture(userPictureFileCode));


        profilePicturDiv.add(
                userProfilePicture,
                buttonLayout
        );
        return profilePicturDiv;
    }

    private Div createPersonalDataDiv() {
        final String nameFieldText = messageSource.getMessage(LocalizationUtils.UI.UserProfileView.NAME_FIELD_TEXT, null, locale);
        final String surnameFieldText = messageSource.getMessage(LocalizationUtils.UI.UserProfileView.SURNAME_FIELD_TEXT, null, locale);
        final String middleNameFieldText = messageSource.getMessage(LocalizationUtils.UI.UserProfileView.MIDDLE_NAME_FIELD_TEXT, null, locale);
        final String phoneFieldText = messageSource.getMessage(LocalizationUtils.UI.UserProfileView.PHONE_FIELD_TEXT, null, locale);
        final String emailFieldText = messageSource.getMessage(LocalizationUtils.UI.UserProfileView.EMAIL_FIELD_TEXT, null, locale);
        final String birthDateFieldText = messageSource.getMessage(LocalizationUtils.UI.UserProfileView.BIRTHDATE_FIELD_TEXT, null, locale);

        final String nameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_USER_NAME_ERROR, null, locale);
        final String surnameValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_SURNAME_ERROR, null, locale);
        final String phoneValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_PHONE_ERROR, null, locale);
        final String emailValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_EMAIL_ERROR, null, locale);
        final String birthDateValidationText = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_BIRTHDATE_ERROR, null, locale);

        final H3Header personalDataHeader = new H3Header(messageSource, locale, LocalizationUtils.UI.UserProfileView.PERSONAL_DATA_HEADER_TEXT);

        final TextField nameField = new TextField(nameFieldText);
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);
        updateUserBinder.forField(nameField)
                .withValidator(name -> !StringUtils.isBlank(name), nameValidationText)
                .bind(UpdateUserRequest::getName, UpdateUserRequest::setName);

        final TextField surnameField = new TextField(surnameFieldText);
        surnameField.setRequired(true);
        surnameField.setRequiredIndicatorVisible(true);
        updateUserBinder.forField(surnameField)
                .withValidator(surname -> !StringUtils.isBlank(surname), surnameValidationText)
                .bind(UpdateUserRequest::getSurname, UpdateUserRequest::setSurname);

        final TextField middleNameField = new TextField(middleNameFieldText);
        updateUserBinder.forField(middleNameField)
                .bind(UpdateUserRequest::getMiddleName, UpdateUserRequest::setMiddleName);

        final TextField phoneField = new TextField(phoneFieldText);
        phoneField.setRequired(true);
        phoneField.setRequiredIndicatorVisible(true);
        updateUserBinder.forField(phoneField)
                .withValidator(new RegexpValidator(phoneValidationText, ValidationUtils.NUMBER_REGEX))
                .bind(UpdateUserRequest::getPhone, UpdateUserRequest::setPhone);

        final EmailField emailField = new EmailField(emailFieldText);
        emailField.setRequiredIndicatorVisible(true);
        updateUserBinder.forField(emailField)
                .withValidator(new RegexpValidator(emailValidationText, ValidationUtils.EMAIL_REGEX))
                .bind(UpdateUserRequest::getEmail, UpdateUserRequest::setEmail);

        final DatePicker birthDateField = new DatePicker(birthDateFieldText);
        birthDateField.setRequired(true);
        birthDateField.setRequiredIndicatorVisible(true);
        updateUserBinder.forField(birthDateField)
                .withValidator(Objects::nonNull, birthDateValidationText)
                .bind(UpdateUserRequest::getBirthDate, UpdateUserRequest::setBirthDate);

        final DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
        singleFormatI18n.setDateFormat(DateUtils.DEFAULT_DATE_FORMAT);
        birthDateField.setI18n(singleFormatI18n);

        final SaveButton saveButton = new SaveButton(messageSource, locale, this.updateUserPersonalData());
        final HorizontalLayout buttonLayout = new HorizontalLayout(saveButton);
        buttonLayout.setAlignItems(Alignment.START);
        buttonLayout.setJustifyContentMode(JustifyContentMode.START);

        final FormLayout personalDataForm = new FormLayout();

        personalDataForm.add(nameField, surnameField, middleNameField,
                phoneField, emailField, birthDateField, buttonLayout);
        personalDataForm.setResponsiveSteps(new FormLayout.ResponsiveStep("200px", 1));
        return new Div(personalDataHeader, personalDataForm);
    }

    private ComponentEventListener<ClickEvent<Button>> updateUserPersonalData() {
        return event -> {
            if (updateUserBinder.writeBeanIfValid(updateUserRequest)) {
                userService.updateUser(contextUser.getId(), updateUserRequest);
                NotificationFactory.success(messageSource.getMessage(PERSONAL_DATA_SUCCESSFULLY_UPDATED, null, locale)).open();
            }
        };
    }

    private ComponentEventListener<ClickEvent<Button>> updateUserPicture(final AtomicReference<Optional<FileMetaDataDto>> atomicUploadedPicture) {
        return event -> {
            final Optional<FileMetaDataDto> optionalFileMetaDataDto = atomicUploadedPicture.get();
            if (Objects.nonNull(optionalFileMetaDataDto) && optionalFileMetaDataDto.isPresent()) {
                userService.updateUserPicture(contextUser.getId(), optionalFileMetaDataDto.get());
                NotificationFactory.success(messageSource.getMessage(PICTURE_SUCCESSFULLY_UPDATED, null, locale)).open();
                UI.getCurrent().getPage().reload();
            }
        };
    }

    private ComponentEventListener<ClickEvent<Button>> deleteUserPicture(final String userPictureFileCode) {
        return buttonClickEvent -> {
            if (Objects.nonNull(userPictureFileCode) && !StringUtils.isBlank(userPictureFileCode)) {
                fileService.delete(userPictureFileCode);
                userService.updateUserPicture(contextUser.getId(), null);
                NotificationFactory.success(messageSource.getMessage(PICTURE_SUCCESSFULLY_DELETED, null, locale)).open();
                UI.getCurrent().getPage().reload();
            }
        };
    }
}
