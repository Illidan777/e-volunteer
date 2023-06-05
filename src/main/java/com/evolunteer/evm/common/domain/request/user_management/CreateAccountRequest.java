package com.evolunteer.evm.common.domain.request.user_management;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccountRequest {
    private String username;
    private String password;
    private String email;
}
