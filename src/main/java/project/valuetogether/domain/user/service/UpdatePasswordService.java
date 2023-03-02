package project.valuetogether.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.valuetogether.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UpdatePasswordService {

    private final UserRepository userRepository;
    
}
