package com.evolunteer.evm.common.domain.dto.file_management;

import com.evolunteer.evm.common.domain.enums.file_management.FileExtension;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Date: 15.06.22
 *
 * @author ilia
 */
@Data
public class FileMetaDataDto implements Serializable {
    private Integer id;
    private String name;
    private String code;
    private String path;
    private FileExtension fileSuffix;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
