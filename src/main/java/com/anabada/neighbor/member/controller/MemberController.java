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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    public String loginForm(Model model, String errorMessage, HttpServletRequest request) { // 로그인 폼으로 이동
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        model.addAttribute("errorMessage", errorMessage); // 로그인 실패 시 에러메시지
        return "/member/loginForm";
    }

    @PostMapping("/join")
    public String join(Member member) { // 회원가입
        memberService.save(member);
        return "redirect:/member/loginForm";
    }

    @ResponseBody
    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Member test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails.getMember();
    }

    @ResponseBody
    @GetMapping("/admin")
    @Secured("ROLE_ADMIN")
    public ModelAndView admin(ModelAndView mav,Criteria criteria) {
        List<Member> member = memberService.findAllMember(criteria);//멤버리스트
        mav.addObject("member",member);
        mav.addObject("pageMaker", new PageDTO(member.size(), 10, criteria));
        mav.setViewName("admin/memberList");
        return mav;
    }

    @ResponseBody
    @PostMapping("/emailConfirm")//이메일인증
    public String emailConfirm(String memberEmail) throws Exception {
//        String confirm = emailService.sendSimpleMessage(memberEmail);
        return "confirm";
    }

    @GetMapping("/myWrite")//내가 작성한 글
    public String myWrite(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, Criteria criteria) {
        List<Used> used = memberService.myWrite(principalDetails, criteria);
        int total = memberService.getTotal(principalDetails.getMember().getMemberId());
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
