package com.evolunteer.evm.common.domain.exception.common;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
