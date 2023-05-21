package com.evolunteer.evm.backend.service.file_management.impl;

import com.evolunteer.evm.backend.repository.file_management.FileMetaDataRepository;
import com.evolunteer.evm.backend.service.file_management.FileService;
import com.evolunteer.evm.backend.service.file_management.validator.FileValidator;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.general.Pair;
import com.evolunteer.evm.common.domain.entity.file_management.FileMetaData;
import com.evolunteer.evm.common.domain.enums.file_management.FileExtension;
import com.evolunteer.evm.common.domain.enums.file_management.FileStatus;
import com.evolunteer.evm.common.domain.exception.common.ResourceNotFoundException;
import com.evolunteer.evm.common.domain.exception.file.InvalidFileException;
import com.evolunteer.evm.common.domain.exception.validation.ValidationException;
import com.evolunteer.evm.common.mapper.file_management.FileMetaDataMapper;
import com.evolunteer.evm.common.utils.file.FileUtils;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import com.evolunteer.evm.common.utils.string.StringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Integer DEFAULT_FILE_CODE_LENGTH = 20;

    private final FileMetaDataRepository fileMetaDataRepository;
    private final FileMetaDataMapper fileMetaDataMapper;

    @Value("${file.root.path}")
    private String rootPath;

    @Transactional
    @Override
    public FileMetaDataDto upload(final byte[] content,
                                  final String filename,
                                  final FileValidator fileValidator) {
        if (Objects.isNull(content) || org.apache.commons.lang3.StringUtils.isBlank(filename)) {
            throw new IllegalArgumentException("Unable to upload file. File content can not be null");
        }
        if(org.apache.commons.lang3.StringUtils.isBlank(filename)) {
            throw new InvalidFileException("Unable to upload file. Filename cannot be absent.", LocalizationUtils.Error.VALIDATION_FILE_EMPTY_FILENAME_ERROR);
        }
        final Pair<FileMetaDataDto, OutputStream> createdFile = this.createFile(filename, fileValidator);
        FileUtils.writeContent(createdFile.getValue(), content);
        return createdFile.getKey();
    }


    @Override
    public FileMetaDataDto download(final OutputStream outputStream, final String code) {
        final FileMetaData fileInfo = this.getFileMetaDataByCode(code);
        FileUtils.readContentFromFile(code, outputStream);
        return fileMetaDataMapper.mapFileToFileDto(fileInfo);
    }

    @Transactional
    @Override
    public void delete(final String code) {
        final FileMetaData fileInfo = this.getFileMetaDataByCode(code);
        FileUtils.delete(fileInfo.getPath());
        fileMetaDataRepository.updateFileStatusByCode(FileStatus.DELETED, code);
    }


    @Override
    public boolean existsByCode(final String code) {
        if (org.apache.commons.lang3.StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Unable to check existence of file. File code cannot be absent.");
        }
        final Optional<FileMetaData> optionalFileInfo = fileMetaDataRepository.findByCode(code);
        if (optionalFileInfo.isEmpty()) {
            return false;
        } else {
            final FileMetaData fileInfo = optionalFileInfo.get();
            return FileUtils.exists(fileInfo.getPath());
        }
    }

    @Override
    public FileMetaDataDto getFileMetaData(final String code) {
        return fileMetaDataMapper.mapFileToFileDto(this.getFileMetaDataByCode(code));
    }

    private Pair<FileMetaDataDto, OutputStream> createFile(final String filename, final FileValidator fileValidator) {
        if (org.apache.commons.lang3.StringUtils.isBlank(filename)) {
            throw new InvalidFileException("Unable to upload file. Filename cannot be absent.", LocalizationUtils.Error.VALIDATION_FILE_EMPTY_FILENAME_ERROR);
        }

        final FileExtension fileExtension = FileExtension.getFileExtensionFromNFilename(filename);
        final String originalFilename = StringUtils.cleanPath(filename);
        final String generatedFileCode = new StringGenerator.StringGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useLower(true)
                .useUpper(true)
                .build()
                .generate(DEFAULT_FILE_CODE_LENGTH);
        final String generatedFilename = generatedFileCode + DEFAULT_EXTENSION_DELIMITER + fileExtension.getValue();
        final String absolutePath = this.buildAbsolutePath(generatedFilename);


        final FileMetaData newFile = new FileMetaData();
        newFile.setName(originalFilename);
        newFile.setCode(generatedFileCode);
        newFile.setFileExtension(fileExtension);
        newFile.setPath(absolutePath);

        if (!fileValidator.isValid(newFile)) {
            log.error("File is invalid! {}", fileValidator.getMessage());
            throw new InvalidFileException(fileValidator.getMessage(), fileValidator.getLocalizedMessageCode());
        }

        final FileMetaData savedFile = fileMetaDataRepository.save(newFile);
        final OutputStream outputStream = FileUtils.createFile(absolutePath);
        return Pair.of(fileMetaDataMapper.mapFileToFileDto(savedFile), outputStream);
    }

    private FileMetaData getFileMetaDataByCode(final String code) {
        if (org.apache.commons.lang3.StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("File code cannot be absent.");
        }
        final FileMetaData fileInfo = fileMetaDataRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(format("File does not exist by code: %s", code)));

        if (fileInfo.isNonActive()) {
            throw new ValidationException(format("Unable to process file. File status: %s", fileInfo.getStatus()));
        }
        return fileInfo;
    }

    private String buildAbsolutePath(final String generatedFilename) {
        return rootPath + File.separator + generatedFilename;
    }
}
