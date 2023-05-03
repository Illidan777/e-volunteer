package com.evolunteer.evm.common.domain.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data(staticConstructor = "of")
public class ApiError {
    private String field;
    private String message;
}
