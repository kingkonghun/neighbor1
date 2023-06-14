package com.anabada.neighbor.member.controller;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.service.EmailService;
import com.anabada.neighbor.member.service.MemberService;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.page.PageDTO;
import com.anabada.neighbor.used.domain.Used;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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


    @GetMapping("/emailConfirm")//이메일인증
    public String emailConfirm() throws Exception {
        String confirm = emailService.sendSimpleMessage("wbg030281@gmail.com");
        return confirm;
    }



    @GetMapping("/myWrite")//내가 작성한 글
    public String myWrite(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, Criteria criteria) {
        List<Used> used = memberService.myWrite(principalDetails, criteria);
        int total = memberService.getTotal(principalDetails);
        model.addAttribute("writeList", used);
        model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
        return "member/myWrite";
    }

    @GetMapping("/myWriteFive")//내가 작성한 글 5개만
    public String myWriteFive(long memberId,Model model) {
        List<Used> used = memberService.myWriteFive(memberId);
        model.addAttribute("list",used);
        return "member/myWriteFive";
    }

    @GetMapping("/myInfo")//내 개인정보
    public String myInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = memberService.myInfo(principalDetails);
        model.addAttribute("list", member);
        return "member/myInfo";
    }

    @ResponseBody
    @GetMapping("/isAuthenticated")//로그인인증
    public boolean isAuthenticated(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails != null;
    }

    @GetMapping("/findProfileImg")//프사다운
    public void findProfileImg(long memberId, HttpServletResponse response) throws Exception {
        String profileImg = memberService.findProfileImg(memberId);//사진이름가져오기
        memberService.downProfileImg(response, profileImg);//사진다운
    }
    @GetMapping("/editInfo")//수정페이지로 이동
    public String editInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = memberService.myInfo(principalDetails);
        model.addAttribute("list", member);
        return "member/editInfo";
    }
    @PostMapping("/myEdit")//진짜수정
    public String myEdit(Member member){
        memberService.editInfo(member);
        return "index";
    }
}
