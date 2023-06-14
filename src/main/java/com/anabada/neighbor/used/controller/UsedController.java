package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
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

@Controller
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {

    private final UsedService usedService;

    @GetMapping("/list") //게시물 리스트
    public String list(@RequestParam(value = "categoryId", defaultValue = "0") long categoryId, Model model, @RequestParam(value = "num", defaultValue = "0") int num){
        model.addAttribute("list", usedService.list(categoryId, "list",num));
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("categoryId", categoryId);
        System.out.println(categoryId);

        return num <= 0 ? "used/list" : "used/listPlus";
    }

    @GetMapping("/detail") //게시물 상세보기
    public String detail(long postId, Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Used dto = usedService.detail(postId, request, response, principalDetails);
        model.addAttribute("dto", dto);
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("similarList", usedService.list(dto.getCategoryId(), "similarList",0));

        return "used/detailEx";
    }

    @PostMapping("/post") //게시물 작성
    public String post(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails)throws Exception{
        usedService.write(used, principalDetails);
        System.out.println("used="+used);
        return "redirect:/used/list";
    }

    @GetMapping("/findImg") //이미지 찾기
    public void findImg(long postId, HttpServletResponse response) throws IOException{
        String filenames = usedService.findImgUrl(postId);
//        System.out.println("filenames = " + filenames);
        usedService.downloadFiles(filenames,response);

    }

    @PostMapping("/postEdit") //게시물 수정
    public String postEdit(Used used, @AuthenticationPrincipal PrincipalDetails principalDetails)throws Exception{
        usedService.update(used, principalDetails);
        return "redirect:/used/list";
    }

    @GetMapping("/postDelete") //게시물 삭제
    public String postDelete(long postId){
        usedService.delete(postId);
        return "redirect:/used/list";
    }

    @PostMapping("/likes")
    @ResponseBody
    public Used likes(long postId, int likesCheck, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return usedService.likes(postId, principalDetails, likesCheck);
    }
}
