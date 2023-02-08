package project.valuetogether.global.exception;

import project.valuetogether.global.security.error.ErrorCode;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException() {
        super(ErrorCode.INVALID_JWT.getMessage());
    }
}
