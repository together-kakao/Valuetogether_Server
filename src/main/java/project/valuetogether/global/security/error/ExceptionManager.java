package project.valuetogether.global.security.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    
    @ExceptionHandler(ValuetogetherException.class)
    public ResponseEntity<ErrorResponse> valuetogetherExceptionHandler(ValuetogetherException e) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(e.getErrorCode().getStatus())
                        .message(e.getErrorCode().getMessage())
                        .build(),
                HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }
}
