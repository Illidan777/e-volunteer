package com.evolunteer.evm.common.domain.dto.user_management;

import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private String phone;
    private String email;
    private Date birthDate;
    private FileMetaDataDto picture;
    private AccountDto accountDetails;
    private BaseFundDto fund;
}
