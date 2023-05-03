package com.evolunteer.evm.common.domain.exception;

import com.evolunteer.evm.common.domain.api.ApiError;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends RuntimeException {

    private final List<ApiError> errors;

    public ValidationException(List<ApiError> errors) {
        super(errors.stream().map(error -> error.getField() + " " + error.getMessage()).collect(Collectors.joining(",")));
        this.errors = errors;
    }

    public ValidationException(String message) {
        super(message);
        this.errors = null;
    }

    public ValidationException(String message, List<ApiError> errors) {
        super(message);
        this.errors = errors;
    }
}
