package com.anabada.neighbor.member.controller;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.file.service.FileUtils;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.service.EmailService;
import com.anabada.neighbor.member.service.MemberService;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.page.PageDTO;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final UsedService usedService;
    private final EmailService emailService;
    private final FileUtils fileUtils;
    private final ClubService clubService;

    @GetMapping("/joinForm") // 회원가입 폼으로 이동
    public String joinForm() {
        return "member/joinForm";
    }

    @GetMapping("/loginForm") // 로그인 폼으로 이동
    public String loginForm(Model model, String errorMessage, HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        model.addAttribute("errorMessage", errorMessage); // 로그인 실패 시 에러메시지
        return "member/loginForm";
    }

    @PostMapping("/join") // 회원가입
    public String join(Member member) {
        memberService.save(member);
        return "redirect:/member/loginForm";
    }

    @GetMapping("admin") // 관리자 페이지로 이동
    @Secured("ROLE_ADMIN")
    public String admin(){
        return "admin/admin";
    }

    @ResponseBody
    @GetMapping("/memberList") // 회원목록
    @Secured("ROLE_ADMIN")
    public ModelAndView memberList(ModelAndView mav,Criteria criteria) {
        int total = memberService.countMember(); // 멤버의 총 수
        List<Member> member = memberService.findAllMember(criteria); // 멤버리스트
        System.out.println("member = " + member);
        System.out.println(total);
        mav.addObject("member",member);
        mav.addObject("pageMaker", new PageDTO(total, 10, criteria));
        mav.setViewName("admin/memberList");
        return mav;
    }

    @ResponseBody
    @PostMapping("/emailConfirm") // 이메일인증
    public String emailConfirm(String memberEmail) throws Exception {
        String confirm = emailService.sendSimpleMessage(memberEmail);
        return confirm;
    }
    @ResponseBody
    @GetMapping("/emailCheck")
    public boolean emailCheck(String memberEmail){ // 이메일 중복체크
        boolean emailCk = memberService.emailCk(memberEmail);
        return emailCk;
    }

    @GetMapping("/myWrite") // 내가 작성한 글
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String myWrite(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, Criteria criteria,
                          @RequestParam(defaultValue = "used" ,value = "postType") String postType) {
        List<Used> used = memberService.myUsedWrite(principalDetails, criteria);
        List<ClubResponse> club = memberService.myClubWrite(principalDetails, criteria);

        int total = 0;
        if (postType.equals("used")) {
            if (used != null) {
                for (Used used1 : used) {
                    List<FileInfo> fileInfoList = fileUtils.getFileInfo(used1.getFileResponseList());
                    used1.setFileInfo(fileInfoList.get(0));
                }
            }
            total = memberService.getUsedTotal(principalDetails.getMember().getMemberId());
            model.addAttribute("writeList", used);
            model.addAttribute("categoryName", "used");
            model.addAttribute("postType", postType);
            model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
        } else if (postType.equals("club")) {
            if (club != null) {
                for (ClubResponse clubResponse : club) {
                    List<FileInfo> fileInfoList = fileUtils.getFileInfo(clubResponse.getFileResponseList());
                    clubResponse.setFileInfo(fileInfoList.get(0));
                }
            }
            total = memberService.getClubTotal(principalDetails.getMember().getMemberId());
            model.addAttribute("writeList", club);
            model.addAttribute("categoryName", "club");
            model.addAttribute("postType", postType);
            model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
        }
        return "member/myWrite";
    }

    @GetMapping("/myInfo") // 마이페이지
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String myInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = memberService.myInfo(principalDetails.getMember().getMemberId());
        model.addAttribute("list", member);
        return "member/myInfoEx";
    }

    @ResponseBody
    @GetMapping("/isAuthenticated") // 로그인인증
    public boolean isAuthenticated(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails != null;
    }

    @ResponseBody
    @GetMapping("/isAuthorization") // 게스트인지 확인(소셜로그인 할 시 게스트 권한)
    public boolean isAuthorization(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails.getMember().getRole().equals("ROLE_GUEST");
    }

    @GetMapping("/findProfileImg") // 프로필사진 보기
    public void findProfileImg(long memberId, HttpServletResponse response) throws Exception {
        String profileImg = memberService.findProfileImg(memberId);//사진이름가져오기
        memberService.downProfileImg(response, profileImg);//사진다운
    }

    @GetMapping("/editInfo")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") // 수정페이지로 이동
    public String editInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model,String message) {
        Member member = memberService.myInfo(principalDetails.getMember().getMemberId());
        model.addAttribute("list", member);
        model.addAttribute("message",message);
        return "member/editInfo";
    }
    @GetMapping("/likePost")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") // 좋아요 누른 게시물
    public String likePost(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails, Criteria criteria) {
        List<Used> usedList=usedService.likePost(principalDetails.getMember().getMemberId(),criteria);
        int total = usedService.countMyUsedLikePost(principalDetails.getMember().getMemberId());
        System.out.println("total = " + total);
            model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
            model.addAttribute("list",usedList);


        return "member/myLikes";
    }
    @PostMapping("/editMyInfo") // 개인정보수정
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String editInfo( Member member,Model model){
        memberService.editInfo(member);
        System.out.println("member = " + member);
        model.addAttribute("msg","infoSuccess");
        return  "index";
    }

    @PostMapping("/editPwd") // 비밀번호 수정
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String editPwd(@RequestParam String oldPwd, @RequestParam String memberPWD, long memberId, Model model, RedirectAttributes redirectAttributes){
        String msg= memberService.editPwd(oldPwd,memberPWD,memberId);
        System.out.println("msg = " + msg);

        if (msg.equals("pwdSuccess")) {
            model.addAttribute("msg", msg);
            return "index";
        } else {
            redirectAttributes.addAttribute("msg", msg);
            return "redirect:/member/editInfo";
        }
    }

    @PostMapping("/editPhoto") // 프로필사진 수정
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String editPhoto(Member member,RedirectAttributes redirectAttributes) {
        memberService.editPhoto(member);
        redirectAttributes.addAttribute("msg", "photoSuccess");
        return  "redirect:/member/editInfo";
    }

//    @GetMapping("/slideBar")
//    @ResponseBody
//    public Member slideBar(@AuthenticationPrincipal PrincipalDetails principalDetails) {
//        Member member = memberService.myInfo(principalDetails.getMember().getMemberId());
//        return member;
//    }

    @ResponseBody
    @GetMapping("/noAdmin") // 일반회원이 관리자페이지 입장시
    public String noAdmin() {
        return "<h1>권한이 없습니다</h1>";
    }
}
