package com.evolunteer.evm.common.utils.file;

import com.evolunteer.evm.common.domain.exception.file.DeleteFileException;
import com.evolunteer.evm.common.domain.exception.file.ReadFileContentException;
import com.evolunteer.evm.common.domain.exception.file.WriteFileContentException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Date: 05.08.22
 *
 * @author ilia
 */
@Slf4j
public class FileUtils {

    public static OutputStream createFile(final String path) {
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("Unable to create file. File path cannot be absent.");
        }
        try {
            return new FileOutputStream(path);
        } catch (IOException e) {
            log.error("Create file exception!", e);
            throw new WriteFileContentException(e.getMessage());
        }
    }

    public static void writeContent(final OutputStream outputStream, final byte[] content) {
        if (Objects.isNull(outputStream) || Objects.isNull(content)) {
            throw new IllegalArgumentException("Unable to write to stream. Stream and content cannot be absent.");
        }
        try (final BufferedOutputStream fileOutputStream = new BufferedOutputStream(outputStream)) {
            fileOutputStream.write(content);
        } catch (IOException e) {
            log.error("Write content to file exception!", e);
            throw new WriteFileContentException(e.getMessage());
        }
    }

    public static void writeContent(final String path, final byte[] content) {
        if (StringUtils.isBlank(path) || Objects.isNull(content)) {
            throw new IllegalArgumentException("Unable to delete file. File path and content cannot be absent.");
        }
        try (final BufferedOutputStream fileOutputStream = new BufferedOutputStream(createFile(path))) {
            fileOutputStream.write(content);
        } catch (IOException e) {
            log.error("Write content to file exception!", e);
            throw new WriteFileContentException(e.getMessage());
        }
    }

    public static void readContentFromFile(final String path, final OutputStream outputStream) {
        if (StringUtils.isBlank(path) || Objects.isNull(outputStream)) {
            throw new IllegalArgumentException("Unable to read file. File path and output stream cannot be absent.");
        }
        try (final BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(path))) {
            IOUtils.copy(fileInputStream, outputStream);
        } catch (IOException e) {
            log.error("Reading content from file exception!", e);
            throw new ReadFileContentException(e.getMessage());
        }
    }

    public static void delete(final String path) {
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("Unable to delete file. File path cannot be absent.");
        }
        final Path filePath = Paths.get(path);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Deleting file exception!", e);
            throw new DeleteFileException("An occurred some problem with file deleting.");
        }
    }

    public static boolean exists(final String path) {
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("Unable to check file existence. File path cannot be absent.");
        }
        return Files.exists(Paths.get(path));
    }
}
