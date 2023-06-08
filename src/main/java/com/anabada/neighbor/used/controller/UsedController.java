package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.lang.model.SourceVersion;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {
    private final UsedService usedService;

    @GetMapping("/list") //게시물 리스트
    public String list(@RequestParam(value = "categoryId", defaultValue = "0") long categoryId, Model model,@RequestParam(value="num",defaultValue = "0")int num){
        model.addAttribute("list", usedService.list(categoryId, "list",num));
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("categoryId", categoryId);

        return num <= 0 ? "used/list" : "used/listPlus";
    }




    @GetMapping("/detail") //게시물 상세보기
    public String detail(long postId, Model model, HttpServletRequest request, HttpServletResponse response) {
        Used dto = usedService.detail(postId, request, response);
        model.addAttribute("dto", dto);
        model.addAttribute("category",usedService.categoryList());
        model.addAttribute("similarList", usedService.list(dto.getCategoryId(), "similarList",0));
        return "used/detail";
    }
    @PostMapping("/post") //게시물 작성
    public String post(Used used, HttpSession session)throws Exception{
        long memberId = (long)session.getAttribute("memberId");
        used.setMemberId(memberId);
        usedService.write(used);
        return "redirect:/used/list";
    }

    @GetMapping("/findImg") //이미지 찾기
    public void findImg(long postId, HttpServletResponse response) throws IOException{
        String filenames = usedService.findImgUrl(postId);
        usedService.downloadFiles(filenames,response);

    }

    @PostMapping("/postEdit") //게시물 수정
    public String postEdit(Used used,HttpSession session)throws Exception{
        long memberId = (long) session.getAttribute("memberId");
        used.setMemberId(memberId);
        usedService.update(used);
        return "redirect:/used/list";

    }

    @GetMapping("/postDelete") //게시물 삭제
    public String postDelete(long postId){
        usedService.delete(postId);
        return "redirect:/used/list";
    }








}
