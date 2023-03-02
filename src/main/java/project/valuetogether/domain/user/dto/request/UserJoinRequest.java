package project.valuetogether.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.valuetogether.global.enums.Gender;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private Gender gender;
    private String nickname;
    private String birthYear;
}
