package com.example.ms1.note.Member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Getter
    @Setter
    public class MemberForm{
        @NotEmpty(message = "아이디는 필수 항목입니다")
        private String loginId;
        @NotEmpty(message = "비밀번호는 필수 항목입니다")
        private String password;
        @NotEmpty(message = "닉네임은 필수 항목입니다")
        private String nickname;
        @NotEmpty(message = "이메일은 필수 항목입니다")
        @Email(message = "이메일 형식을 확인하세요")
        private String email;
    }

    @GetMapping("/signup")
    public String signup(MemberForm memberForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        memberService.save(memberForm.loginId,memberForm.password,memberForm.nickname,memberForm.email);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
