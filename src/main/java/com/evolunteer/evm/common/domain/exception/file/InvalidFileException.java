package com.evolunteer.evm.common.domain.exception.file;

public class InvalidFileException extends RuntimeException {

    private final String localizedMessageCode;

    public InvalidFileException(String message, String localizedMessageCode) {
        super(message);
        this.localizedMessageCode = localizedMessageCode;
    }

    @Override
    public String getLocalizedMessage() {
        return this.localizedMessageCode;
    }
}
