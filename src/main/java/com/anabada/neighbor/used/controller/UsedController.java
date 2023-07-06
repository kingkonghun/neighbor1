package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.file.service.FileService;
import com.anabada.neighbor.file.service.FileUtils;
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
    private final FileService fileService;
    private final FileUtils fileUtils;

    @GetMapping("/list") //게시물 리스트
    public String list(@RequestParam(value = "categoryId", defaultValue = "0") long categoryId, Model model, @RequestParam(value = "num", defaultValue = "0") int num, @RequestParam(value = "search", defaultValue = "") String search) {
        List<Used> list = usedService.list(categoryId, "list", num, search, 0);
        if (list != null) {
            for (Used used : list) {
                List<FileInfo> fileInfoList = fileUtils.getFileInfo(used.getFileResponseList());
                used.setFileInfo(fileInfoList.get(0));
            }
        }

        model.addAttribute("list", list);
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("search", search);

        return num <= 0 ? "used/list" : "used/listPlus";
    }

    @GetMapping("/detail") //게시물 상세보기
    public String detail(long postId, Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Used dto = usedService.detail(postId, request, response, principalDetails);
        List<Used> similarList = usedService.list(dto.getCategoryId(), "similarList", 0, "", postId);
        List<FileResponse> files = dto.getFileResponseList();
        List<FileInfo> fileInfoList = fileUtils.getFileInfo(files);

        if (similarList != null) {
            for (Used used : similarList) {
                List<FileInfo> fileInfoList2 = fileUtils.getFileInfo(used.getFileResponseList());
                used.setFileInfo(fileInfoList2.get(0));
            }
        }

        model.addAttribute("dto", dto);
        model.addAttribute("images", fileInfoList);
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("similarList",similarList);
        model.addAttribute("reportType", usedService.reportType());

        return dto.getPostType().equals("del") ? "redirect:/used/postDel" : "/used/usedDetail";
    }

    @PostMapping("/post") //게시물 작성
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String post(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        usedService.write(used, principalDetails);
        return "redirect:/used/list";
    }




    @PostMapping("/postEdit") //게시물 수정
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String postEdit(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        System.out.println("used = " + used);
        usedService.update(used, principalDetails);
        return "redirect:/used/list";
    }

    @PostMapping("/postDelete") //게시물 삭제
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String postDelete(long postId) {
        usedService.delete(postId);
        return "redirect:/used/list";
    }

    @PostMapping("/likes") //게시물 좋아요
    @ResponseBody
    public Used likes(long postId, int likesCheck, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return usedService.likes(postId, principalDetails, likesCheck);
    }

    @PostMapping("/report") //게시물 신고
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



    @PostMapping("/reportOk") // 신고 접수
    public ResponseEntity<Void> reportOk(ReportOk reportOk) {
        usedService.reportOk(reportOk);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/success")
    public ResponseEntity<Void> soldOut(long postId, long receiver, @AuthenticationPrincipal PrincipalDetails principalDetails){
        usedService.soldOut(postId, receiver, principalDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/purchase")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String purchase(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails,Criteria criteria) {
        List<Used> purchase = usedService.purchase(principalDetails,criteria);
        int total = usedService.countPurchase(principalDetails.getMember().getMemberId());

        model.addAttribute("purchase", purchase);
        model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));

        return "used/purchaseList";
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String sales(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails,Criteria criteria) {
        int total = usedService.countSales(principalDetails.getMember().getMemberId());
        model.addAttribute("sales", usedService.sales(principalDetails,criteria));
        model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
        return "used/salesList";
    }

    @GetMapping("/trade")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String trade() {
        return "used/trade";
    }

    @GetMapping("/postDel")
    @ResponseBody
    public String postDel() {
        return "<h1>삭제된 게시물입니다.</h1>";
    }
}
