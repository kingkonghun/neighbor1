package com.anabada.neighbor.member.controller;

import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/login")
    public String login(){
        return "/member/login";
    }
    @PostMapping("/loginMember.do")
    public ModelAndView loginMember(ModelAndView mav,Member member, HttpSession session){

        int result= memberService.login(member,session);
        if(result==1){//로그인성공
            mav.setViewName("index");
        }else{
            mav.setViewName("member/join");
        }
        return mav;
    }
    @GetMapping("/join")
    public String join(){
        return "member/join";
    }
    @PostMapping("/joinMember.do")
    public String joinMember(Member member,String m, String b, String t, String i){
        String getMbti = m+b+t+i;
        member.setMbti(getMbti);
        memberService.join(member);
        return "index";
    }
    @GetMapping("/logout")//로그아웃
    public String logout(HttpSession session){
        memberService.logout(session);
        return "index";
    }


}
