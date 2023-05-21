package com.evolunteer.evm.common.mapper.file_management;

import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.entity.file_management.FileMetaData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class FileMetaDataMapper {

    public abstract FileMetaData mapFileDtoToFile(FileMetaDataDto fileInfoDto);


    public abstract FileMetaDataDto mapFileToFileDto(FileMetaData fileInfo);
}
