package com.anabada.neighbor.member.used.controller;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.used.service.UsedService;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.page.PageDTO;
import com.anabada.neighbor.used.domain.PostReport;
import com.anabada.neighbor.member.used.domain.Report;
import com.anabada.neighbor.member.used.domain.Used;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {

    private final UsedService usedService;


    @GetMapping("/list") //게시물 리스트
    public String list(@RequestParam(value = "categoryId", defaultValue = "0") long categoryId, Model model, @RequestParam(value = "num", defaultValue = "0") int num, @RequestParam(value = "search", defaultValue = "") String search){
        model.addAttribute("list", usedService.list(categoryId, "list", num, search));
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("search", search);
        System.out.println("search = " + search);
        System.out.println(categoryId);

        return num <= 0 ? "used/list" : "used/listPlus";
    }

    @GetMapping("/detail") //게시물 상세보기
    public String detail(long postId, Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Used dto = usedService.detail(postId, request, response, principalDetails);
        model.addAttribute("dto", dto);
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("similarList", usedService.list(dto.getCategoryId(), "similarList",0, ""));
        model.addAttribute("reportType", usedService.reportType());
        return "used/detail";
    }

    @PostMapping("/post") //게시물 작성
    public String post(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        usedService.write(used, principalDetails);
        System.out.println("used=" + used);
        return "redirect:/used/list";
    }

    @GetMapping("/findImg") //이미지 찾기
    public void findImg(long postId, HttpServletResponse response) throws IOException {
        String filenames = usedService.findImgUrl(postId);
//        System.out.println("filenames = " + filenames);
        usedService.downloadFiles(filenames, response);

    }

    @PostMapping("/postEdit") //게시물 수정
    public String postEdit(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        usedService.update(used, principalDetails);
        return "redirect:/used/list";
    }

    @GetMapping("/postDelete") //게시물 삭제
    public String postDelete(long postId) {
        usedService.delete(postId);
        return "redirect:/used/list";
    }

    @PostMapping("/likes")
    @ResponseBody
    public Used likes(long postId, int likesCheck, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return usedService.likes(postId, principalDetails, likesCheck);
    }

    @PostMapping("/report")
    public ResponseEntity<Void> report(Report report, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        usedService.report(report, principalDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/reportList")//신고게시글
    public String report(Model model, Criteria criteria) {
        List<PostReport> reportList = usedService.findAllReport();

        System.out.println("reportList = " + reportList);
        model.addAttribute("list", reportList);
        model.addAttribute("pageMaker", new PageDTO(reportList.size(), 10, criteria));
        return "admin/reportList";
    }

    @GetMapping("/likePost")//좋아요 누른 게시글
    public String likePost(Model model, long memberId) {
        List<Used> usedList=usedService.likePost(memberId);
        model.addAttribute("list",usedList);
        return "member/myLikes";
    }
}