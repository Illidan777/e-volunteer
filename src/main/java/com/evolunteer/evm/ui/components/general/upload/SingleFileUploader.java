package com.evolunteer.evm.ui.components.general.upload;

import com.evolunteer.evm.backend.service.file_management.FileService;
import com.evolunteer.evm.backend.service.file_management.validator.FileValidator;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.enums.file_management.FileExtension;
import com.evolunteer.evm.common.domain.exception.file.InvalidFileException;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.ui.components.general.notification.NotificationFactory;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.FailedEvent;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.util.unit.DataSize;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class SingleFileUploader extends Upload {

    private final MessageSource messageSource;
    private final Locale locale;
    private final FileService fileService;
    private final FileValidator fileValidator;
    private final MemoryBuffer receiver;
    private final Long maxSizeFileLimit;

    public SingleFileUploader(MessageSource messageSource, Locale locale, FileService fileService, FileValidator fileValidator) {
        this.locale = locale;
        this.messageSource = messageSource;
        this.fileService = fileService;
        this.fileValidator = fileValidator;
        this.maxSizeFileLimit = fileValidator.getMaxFileSizeLimit();
        this.receiver = new MemoryBuffer();

        this.setReceiver(receiver);
        this.setAutoUpload(true);
        this.setMaxFiles(1);
        this.setAcceptedFileTypes(fileValidator.getAcceptableFileExtensions().stream()
                .map(extension -> FileService.DEFAULT_EXTENSION_DELIMITER + extension.getValue()).toArray(String[]::new));
        this.addFailedListener(this::handleFailedUpload);
    }

    private void handleFailedUpload(final FailedEvent failedEvent) {
        log.error("Failed file uploading!", failedEvent.getReason());
        NotificationFactory.error(messageSource.getMessage(LocalizationUtils.Error.UPLOADING_FILE_ERROR, null, locale)).open();
        this.clearFileList();
    }

    public Optional<FileMetaDataDto> handleSucceededUpload(final SucceededEvent succeededEvent) {
        final String fileSuccessfullyUploadedMessage = messageSource.getMessage(LocalizationUtils.UI.FileUpload.SUCCESS, null, locale);
        final String invalidFileErrorMessage = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_FILE_ERROR, null, locale);
        final String uploadingFileErrorMessage = messageSource.getMessage(LocalizationUtils.Error.UPLOADING_FILE_ERROR, null, locale);
        final String fileSizeErrorMessage = messageSource.getMessage(LocalizationUtils.Error.VALIDATION_FILE_SIZE_ERROR, new Long[]{DataSize.ofBytes(maxSizeFileLimit).toMegabytes()}, locale);
        if (succeededEvent.getContentLength() >= maxSizeFileLimit) {
            NotificationFactory.error(fileSizeErrorMessage).open();
            this.clearFileList();
            return Optional.empty();
        }
        final String filename = succeededEvent.getFileName();
        final byte[] fileData;
        Notification notification = NotificationFactory.success(fileSuccessfullyUploadedMessage);
        try {
            fileData = receiver.getInputStream().readAllBytes();
        } catch (IOException e) {
            log.error("File uploading error!", e);
            notification = NotificationFactory.error(uploadingFileErrorMessage);
            notification.open();
            this.clearFileList();
            return Optional.empty();
        }
        Optional<FileMetaDataDto> uploadedFile = Optional.empty();
        try {
            uploadedFile = Optional.of(fileService.upload(fileData, filename, fileValidator));
        } catch (InvalidFileException e) {
            log.error("Invalid file!", e);
            final String localizedFileValidationMessage = messageSource.getMessage(e.getLocalizedMessage(), null, locale);
            notification = NotificationFactory.error(localizedFileValidationMessage);
            this.clearFileList();
        } catch (IllegalArgumentException e) {
            log.error("Invalid file!", e);
            notification = NotificationFactory.error(invalidFileErrorMessage);
            this.clearFileList();
        } catch (Exception e) {
            log.error("File uploading error!", e);
            notification = NotificationFactory.error(uploadingFileErrorMessage);
            this.clearFileList();
        }
        notification.open();
        return uploadedFile;
    }
}
