package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.page.PageDTO;
import com.anabada.neighbor.used.domain.PostReport;
import com.anabada.neighbor.used.domain.Report;
import com.anabada.neighbor.used.domain.ReportOk;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
        model.addAttribute("imgCount", dto.getImgList().size());
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("similarList", usedService.list(dto.getCategoryId(), "similarList",0, ""));
        model.addAttribute("reportType", usedService.reportType());

        return "used/usedDetail";
    }


    @PostMapping("/post") //게시물 작성
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String post(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        usedService.write(used, principalDetails);
        return "redirect:/used/list";
    }

    @GetMapping("/findImg") //이미지 찾기
    public void findImg(long postId, HttpServletResponse response) throws IOException {
        String filenames = usedService.findImgUrl(postId);
//        System.out.println("filenames = " + filenames);
        usedService.downloadFiles(filenames, response);
    }
    @GetMapping("/downFiles")//이미지 다운
    public void downFiles(String img,HttpServletResponse response) throws IOException{
        usedService.downloadFiles(img,response);
    }

    @PostMapping("/postEdit") //게시물 수정
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String postEdit(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        usedService.update(used, principalDetails);
        return "redirect:/used/list";
    }

    @GetMapping("/postDelete") //게시물 삭제
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String postDelete(long postId) {
        usedService.delete(postId);
        return "redirect:/used/list";
    }

    @PostMapping("/likes") //게시물 좋아요
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @ResponseBody
    public Used likes(long postId, int likesCheck, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return usedService.likes(postId, principalDetails, likesCheck);
    }

    @PostMapping("/report") //게시물 신고
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> report(Report report, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        usedService.report(report, principalDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/reportList") //신고게시물 리스트
    @Secured("ROLE_ADMIN")
    public String report(Model model, Criteria criteria) {
        List<PostReport> reportList = usedService.findAllReport(criteria);
        int total = usedService.countReport();
        model.addAttribute("list", reportList);
        model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
        return "admin/reportList";
    }

    @GetMapping("/likePost") //좋아요 누른 게시글
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String likePost(Model model, long memberId, Criteria criteria) {
      List<Used> usedList=usedService.likePost(memberId,criteria);
        int total = usedService.countLikePost(memberId);
        model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
      model.addAttribute("list",usedList);
        return "member/myLikes";
    }

    @PostMapping("/reportOk")
    public ResponseEntity<Void> reportOk(ReportOk reportOk) {
        usedService.reportOk(reportOk);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
