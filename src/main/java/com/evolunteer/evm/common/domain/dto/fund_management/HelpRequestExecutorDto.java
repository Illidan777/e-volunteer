package com.evolunteer.evm.common.domain.dto.fund_management;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.EMPTY_STRING;
import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.SPACE;

@Data
public class HelpRequestExecutorDto {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private String organizationName;
    private String phone;
    private String email;

    public String getFullName() {
        return StringUtils.normalizeSpace(
                (Objects.nonNull(this.surname) && !StringUtils.isBlank(this.surname) ? this.surname : EMPTY_STRING) + SPACE
                        + (Objects.nonNull(this.name) && !StringUtils.isBlank(this.name) ? this.name : EMPTY_STRING) + SPACE
                        + (Objects.nonNull(this.middleName) && !StringUtils.isBlank(this.middleName) ? this.middleName : EMPTY_STRING)
        );
    }
}
