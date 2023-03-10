package project.valuetogether.global.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.valuetogether.global.security.repository.UserRepository;

@RequiredArgsConstructor
@RestController
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("token")
    public String token() {
        return "token";
    }
    
    @GetMapping("/user")
    public String user() {
        return "user";
    }

    //manager, admin 권한만 접근 가능
    @GetMapping("/proposer")
    public String manager() {
        return "manager";
    }

    //admin 권한만 접근 가능
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
