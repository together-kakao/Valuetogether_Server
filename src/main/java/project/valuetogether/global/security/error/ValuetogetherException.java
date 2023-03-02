package project.valuetogether.global.security.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ValuetogetherException extends RuntimeException {
    private final ErrorCode errorCode;
}
