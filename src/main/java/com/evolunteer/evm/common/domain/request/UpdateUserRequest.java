package com.evolunteer.evm.common.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Name must be specified!")
    private String name;
    @NotBlank(message = "Surname must be specified!")
    private String surname;
    private String middleName;
    @NotBlank(message = "Phone must be specified!")
    private String phone;
    @NotBlank(message = "Email must be specified!")
    private String email;
    @NotNull(message = "Birth date must be specified!")
    private LocalDate birthDate;
}
