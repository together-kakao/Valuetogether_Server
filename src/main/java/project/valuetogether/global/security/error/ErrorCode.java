package project.valuetogether.global.security.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    UNKNOWN_ERROR("Unknown_Error"),
    EXPIRED_JWT("Expired Jwt"),
    INVALID_JWT("Invalid_Jwt"),
    NON_EXISTENT("Non_Existent");

    private final String message;
}
