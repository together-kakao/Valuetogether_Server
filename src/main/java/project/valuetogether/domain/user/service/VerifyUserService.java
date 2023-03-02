package project.valuetogether.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.valuetogether.domain.user.dto.request.VerifyUserRequest;
import project.valuetogether.domain.user.entity.User;
import project.valuetogether.domain.user.repository.UserRepository;
import project.valuetogether.global.security.error.ErrorCode;
import project.valuetogether.global.security.error.ValuetogetherException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VerifyUserService {

    private final UserRepository userRepository;

    public void verifyUser(VerifyUserRequest request) {
        Optional<User> findUser = userRepository.verifyUser(request.getEmailOrPhonenumber());
        if (findUser.isEmpty()) {
            throw new ValuetogetherException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
