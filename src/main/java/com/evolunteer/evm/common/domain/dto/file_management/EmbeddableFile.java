package com.evolunteer.evm.common.domain.dto.file_management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class EmbeddableFile {
    @NotNull(message = "File code can not be null")
    @NotBlank(message = "File code can not be blank")
    private String fileCode;
    @NotNull(message = "Filename can not be null")
    @NotBlank(message = "Filename can not be blank")
    private String fileName;
}
