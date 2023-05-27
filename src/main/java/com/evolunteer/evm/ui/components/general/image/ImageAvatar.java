package com.evolunteer.evm.ui.components.general.image;

import com.evolunteer.evm.backend.service.file_management.FileService;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class ImageAvatar extends Avatar {

    public ImageAvatar(FileService fileService, String fileCode, String username) {
        Objects.requireNonNull(fileService);
        Objects.requireNonNull(username);

        if (Objects.isNull(fileCode) || StringUtils.isBlank(fileCode) || !fileService.existsByCode(fileCode)) {
            this.setName(username);
        } else {
            final FileMetaDataDto fileMetaData = fileService.getFileMetaData(fileCode);
            final File imageFile = new File(fileMetaData.getPath());
            final StreamResource resource = new StreamResource(fileMetaData.getName(), () -> {
                try {
                    return new FileInputStream(imageFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            setImageResource(resource);
        }
    }
}
