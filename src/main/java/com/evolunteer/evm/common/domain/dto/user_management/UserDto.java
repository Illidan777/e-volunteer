package com.evolunteer.evm.common.domain.dto.user_management;

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
    private AccountDto accountDetails;
}
