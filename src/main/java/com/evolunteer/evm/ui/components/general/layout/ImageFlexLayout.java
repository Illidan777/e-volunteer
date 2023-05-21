package com.evolunteer.evm.ui.components.general.layout;

import com.evolunteer.evm.backend.service.file_management.FileService;
import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class ImageFlexLayout extends FlexLayout {

    private static final String DEFAULT_ALT = "image";

    public ImageFlexLayout(FileService fileService, String fileCode, String alt) {
        final Image image = new Image();
        image.getStyle().set("flex-grow", "1");
        image.getStyle().set("flex-shrink", "1");
        image.getStyle().set("width", "100%");
        image.getStyle().set("height", "auto");

        if (Objects.isNull(fileCode) || StringUtils.isBlank(fileCode) || !fileService.existsByCode(fileCode)) {
            image.setAlt(Objects.isNull(alt) || StringUtils.isBlank(alt) ? DEFAULT_ALT : alt);
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
            image.setSrc(resource);
        }
        this.add(image);
    }
}
