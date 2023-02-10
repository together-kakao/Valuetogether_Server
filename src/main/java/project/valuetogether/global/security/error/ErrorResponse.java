package project.valuetogether.global.security.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class ErrorResponse {

    private final int status;
    private final String message;
}
