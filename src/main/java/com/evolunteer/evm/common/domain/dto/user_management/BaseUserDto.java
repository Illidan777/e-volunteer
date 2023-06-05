package com.evolunteer.evm.common.domain.dto.user_management;

import com.evolunteer.evm.common.domain.dto.file_management.FileMetaDataDto;
import com.evolunteer.evm.common.domain.dto.fund_management.BaseFundDto;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.*;

@Data
public class BaseUserDto {
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

    public String getFullName() {
        return StringUtils.normalizeSpace(
                (Objects.nonNull(this.surname) && !StringUtils.isBlank(this.surname) ? this.surname : EMPTY_STRING) + SPACE
                        + (Objects.nonNull(this.name) && !StringUtils.isBlank(this.name) ? this.name : EMPTY_STRING) + SPACE
                        + (Objects.nonNull(this.middleName) && !StringUtils.isBlank(this.middleName) ? this.middleName : EMPTY_STRING)
        );
    }

    public String getFullNameWithUsername() {
        return this.getFullName() + SPACE + LEFT_BRACKET + accountDetails.getUsername() + RIGHT_BRACKET;
    }
}
