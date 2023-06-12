package com.anabada.neighbor.member.controller;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.service.EmailService;
import com.anabada.neighbor.member.service.MemberService;
import com.anabada.neighbor.used.domain.Used;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping("/joinForm")
    public String joinForm() { // 회원가입 폼으로 이동
        return "member/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm(Model model, String errorMessage) { // 로그인 폼으로 이동
        model.addAttribute("errorMessage", errorMessage); // 로그인 실패 시 에러메시지
        return "/member/loginForm";
    }

    @GetMapping("/addInfoForm")
    public String addInfoForm() { // OAuth 로그인 추가 정보 입력 폼으로 이동
        return "/member/addInfoForm";
    }

    @PostMapping("/join")
    public String join(Member member, String m, String b, String t, String i) { // 회원가입
        member.setMbti(m + b + t + i);
        memberService.save(member);
        return "redirect:/member/loginForm";
    }

    @ResponseBody
    @GetMapping("/test")
    @Secured("ROLE_USER")
    public Member test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails.getMember();
    }

    @ResponseBody
    @GetMapping("/admin")
    @Secured("ROLE_admin")
    public String admin() {
        return "어드민 권한 테스트";
    }


    @GetMapping("/emailConfirm")
    public String emailConfirm() throws Exception {
        String confirm = emailService.sendSimpleMessage("wbg030281@gmail.com");
        return confirm;
    }

    @GetMapping("/myInfo")
    public String myInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model){

        return "member/myPage";
    }

    @GetMapping("/myWrite")//내가 작성한 글
    public String myWrite(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<Used> used = memberService.myWrite(principalDetails);
        model.addAttribute("writeList",used);
        return "member/myWrite";
    }
}
