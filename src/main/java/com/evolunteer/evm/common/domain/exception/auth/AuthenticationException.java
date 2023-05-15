package com.evolunteer.evm.common.domain.exception.auth;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message) {
        super(message);
    }
}
