package com.evolunteer.evm.backend.service.file_management.validator;

import com.evolunteer.evm.common.domain.entity.file_management.FileMetaData;
import com.evolunteer.evm.common.domain.enums.file_management.FileExtension;

import java.util.Set;

public interface FileValidator {

    boolean isValid(FileMetaData fileMetaData);

    String getMessage();

    String getLocalizedMessageCode();

    Long getMaxFileSizeLimit();

    Set<FileExtension> getAcceptableFileExtensions();
}
