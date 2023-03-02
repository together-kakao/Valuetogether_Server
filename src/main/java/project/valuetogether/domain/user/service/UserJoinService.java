package project.valuetogether.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.valuetogether.domain.user.dto.request.UserJoinRequest;
import project.valuetogether.domain.user.entity.User;
import project.valuetogether.domain.user.repository.UserRepository;
import project.valuetogether.global.security.error.ErrorCode;
import project.valuetogether.global.security.error.ValuetogetherException;

@Service
@RequiredArgsConstructor
public class UserJoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void userJoin(UserJoinRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new ValuetogetherException(ErrorCode.ALREADY_EMAIL_EXIST);
                });

        userRepository.save(User.builder()
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .gender(request.getGender())
                .nickname(request.getNickname())
                .birthYear(request.getBirthYear())
                .build());
    }
}
