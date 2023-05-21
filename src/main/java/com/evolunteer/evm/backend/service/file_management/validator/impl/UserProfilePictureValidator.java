package com.evolunteer.evm.backend.service.file_management.validator.impl;

import com.evolunteer.evm.backend.service.file_management.validator.FileValidator;
import com.evolunteer.evm.common.domain.entity.file_management.FileMetaData;
import com.evolunteer.evm.common.domain.enums.file_management.FileExtension;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.util.Set;

@Component
public class UserProfilePictureValidator implements FileValidator {


    @Override
    public boolean isValid(final FileMetaData fileMetaData) {
        return this.getAcceptableFileExtensions().contains(fileMetaData.getFileExtension());
    }

    @Override
    public String getMessage() {
        return String.format("Invalid user picture extension. Allowed formats: %s", this.getAcceptableFileExtensions());
    }

    @Override
    public String getLocalizedMessageCode() {
        return LocalizationUtils.Error.VALIDATION_USER_PROFILE_PICTURE_EXTENSION_ERROR;
    }

    @Override
    public Long getMaxFileSizeLimit() {
        return DataSize.ofMegabytes(10).toBytes();
    }

    @Override
    public Set<FileExtension> getAcceptableFileExtensions() {
        return Set.of(FileExtension.JPG, FileExtension.JPEG, FileExtension.PNG);
    }
}
