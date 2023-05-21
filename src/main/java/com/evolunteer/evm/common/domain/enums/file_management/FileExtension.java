package com.evolunteer.evm.common.domain.enums.file_management;

import com.evolunteer.evm.common.domain.exception.file.InvalidFileException;
import com.evolunteer.evm.common.utils.localization.LocalizationUtils;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import java.util.Locale;

/**
 * Date: 24.06.22
 *
 * @author ilia
 */
@Getter
public enum FileExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    PDF("pdf"),
    DOC("doc"),
    DOCX("docx"),
    CSV("csv"),
    XLS("xls"),
    XLSX("xlsx");

    FileExtension(String value) {
        this.value = value;
    }

    private final String value;

    public static FileExtension getFileExtensionFromNFilename(String filename) {
        return FileExtension.valueOf(validateFilenameAndExtractExtension(filename).toUpperCase(Locale.ROOT));
    }

    private static String validateFilenameAndExtractExtension(final String filename) {
        if (org.apache.commons.lang3.StringUtils.isBlank(filename)) {
            throw new InvalidFileException("Unable to validate file. Filename can not be null or empty.", LocalizationUtils.Error.VALIDATION_FILE_EMPTY_FILENAME_ERROR);
        }
        final String fileExtension = FilenameUtils.getExtension(filename);
        if (org.apache.commons.lang3.StringUtils.isBlank(fileExtension)) {
            throw new InvalidFileException("Unable to validate file. File extension can not be absent.", LocalizationUtils.Error.VALIDATION_FILE_EMPTY_FILE_EXTENSION_ERROR);
        }
        return fileExtension;
    }
}
