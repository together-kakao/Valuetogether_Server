package project.valuetogether.global.exception;

import project.valuetogether.global.security.error.ErrorCode;

public class NonExistentException extends RuntimeException{

    public NonExistentException() {
        super(ErrorCode.NON_EXISTENT.getMessage());
    }
}
