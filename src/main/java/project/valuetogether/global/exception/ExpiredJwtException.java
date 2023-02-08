package project.valuetogether.global.exception;

import project.valuetogether.global.security.error.ErrorCode;

public class ExpiredJwtException extends RuntimeException {
    public ExpiredJwtException() {
        super(ErrorCode.EXPIRED_JWT.getMessage());
    }
}
