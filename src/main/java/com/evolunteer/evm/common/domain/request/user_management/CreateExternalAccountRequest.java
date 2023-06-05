package com.evolunteer.evm.common.domain.request.user_management;

import com.evolunteer.evm.common.domain.enums.user_management.AccountAuthType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CreateExternalAccountRequest {
    @NotBlank(message = "Username must be specified!")
    private String username;
    @NotNull(message = "Auth type must be specified!")
    private AccountAuthType authType;
}
