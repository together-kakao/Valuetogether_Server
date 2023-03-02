package project.valuetogether.global.security.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    //401
    UNKNOWN_ERROR(401, "Unknown Error"),
    EXPIRED_JWT(401, "Expired Jwt"),
    INVALID_JWT(401, "Invalid Jwt"),
    NON_EXISTENT(401, "Non Existent"),

    //404
    USER_NOT_FOUND(404, "User Not Found"),

    //409
    ALREADY_EMAIL_EXIST(409, "Already Email Exist");

    private final int status;
    private final String message;
}
