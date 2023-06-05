package com.evolunteer.evm.common.domain.request.user_management;

import com.evolunteer.evm.common.domain.enums.user_management.AccountAuthType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateExternalUserRequest {
    @NotBlank(message = "Name must be specified!")
    private String name;
    @NotBlank(message = "Surname must be specified!")
    private String surname;
    @NotBlank(message = "Surname must be specified!")
    private String email;
    @NotBlank(message = "Username must be specified!")
    private String username;
    @NotNull(message = "Account auth type must be specified!")
    private AccountAuthType authType;
}
