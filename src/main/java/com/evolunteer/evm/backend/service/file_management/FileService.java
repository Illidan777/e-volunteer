package com.evolunteer.evm.backend.service.file_management;

import com.evolunteer.evm.backend.service.file_management.validator.FileValidator;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;

import java.io.OutputStream;

public interface FileService {

    String DEFAULT_EXTENSION_DELIMITER = ".";

    FileMetaDataDto upload(byte[] content, String filename, FileValidator fileValidator);

    FileMetaDataDto download(OutputStream outputStream, String code);

    void delete(String code);

    boolean existsByCode(String code);

    FileMetaDataDto getFileMetaData(String code);
}
