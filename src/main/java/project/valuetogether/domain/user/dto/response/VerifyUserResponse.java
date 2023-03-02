package project.valuetogether.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerifyUserResponse {
    private String email;
    private String phoneNumber;
}
