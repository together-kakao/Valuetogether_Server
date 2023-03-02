package project.valuetogether.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindAccountRequest {
    private String nicknameOrName;
    private String phoneNumber;
}
