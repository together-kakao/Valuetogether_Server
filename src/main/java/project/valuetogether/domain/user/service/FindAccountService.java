package project.valuetogether.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import project.valuetogether.domain.user.dto.request.FindAccountRequest;
import project.valuetogether.domain.user.dto.response.FindAccountResponse;
import project.valuetogether.domain.user.entity.User;
import project.valuetogether.domain.user.repository.UserRepository;
import project.valuetogether.global.security.error.ErrorCode;
import project.valuetogether.global.security.error.ValuetogetherException;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindAccountService {

    private final UserRepository userRepository;

    public FindAccountResponse findAccount(@RequestBody @Valid FindAccountRequest request) {
        Optional<User> findUser = userRepository.findAccount(request.getNicknameOrName(),
                request.getPhoneNumber());
        if (findUser.isEmpty()) {
            throw new ValuetogetherException(ErrorCode.USER_NOT_FOUND);
        }

        return FindAccountResponse.builder()
                .email(findUser.get().getEmail())
                .phoneNumber(findUser.get().getPhoneNumber())
                .build();
    }
}
