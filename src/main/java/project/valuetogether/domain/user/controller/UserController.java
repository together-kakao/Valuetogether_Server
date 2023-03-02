package project.valuetogether.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.valuetogether.domain.user.dto.request.FindAccountRequest;
import project.valuetogether.domain.user.dto.request.UserJoinRequest;
import project.valuetogether.domain.user.dto.request.VerifyUserRequest;
import project.valuetogether.domain.user.dto.response.FindAccountResponse;
import project.valuetogether.domain.user.service.FindAccountService;
import project.valuetogether.domain.user.service.UserJoinService;
import project.valuetogether.domain.user.service.VerifyUserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserJoinService userJoinService;
    private final FindAccountService findAccountService;
    private final VerifyUserService verifyUserService;

    //회원가입
    @PostMapping("/join")
    public String userJoin(@RequestBody @Valid UserJoinRequest request) {
        userJoinService.userJoin(request);
        return "redirect:/";
    }

    //ID찾기
    @PostMapping("/find_account")
    public FindAccountResponse findAccount(@RequestBody @Valid FindAccountRequest request) {
        return findAccountService.findAccount(request);
    }

    //회원검증하기
    @PostMapping("/verify_user")
    public void verifyUser(@RequestBody @Valid VerifyUserRequest request) {
        verifyUserService.verifyUser(request);
    }

    //비밀번호재설정 -> 회원검증 후 하는곳
}