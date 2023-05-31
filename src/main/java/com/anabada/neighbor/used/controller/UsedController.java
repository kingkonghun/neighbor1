package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/used")
@RequiredArgsConstructor
public class UsedController {
    private final UsedService usedService;

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("list", usedService.list());
        return "used/list";
    }

    @GetMapping("detail")
    public String detail(long postId, Model model) {
        model.addAttribute("dto", usedService.detail(postId));
        return "used/detail";
    }
    @PostMapping("/post")
    public String post(Used used, HttpSession session)throws Exception{
        long memberId = (long)session.getAttribute("memberId");
        used.setMemberId(memberId);
        usedService.write(used);
        return "redirect:/used/list";
    }
    @GetMapping("/findImg")
    public void findImg(long postId, HttpServletResponse response) throws IOException{
        String filenames = usedService.findImgUrl(postId);
        usedService.downloadFiles(filenames,response);

    }

    @PostMapping("/postEdit")
    public String postEdit(Used used,HttpSession session)throws Exception{
        long memberId = (long) session.getAttribute("memberId");
        used.setMemberId(memberId);
        System.out.println("컨트롤러:"+used.getFiles());
        System.out.println("asd:"+used.getFiles().get(0).getOriginalFilename());
        usedService.update(used);
        return "redirect:/used/list";

    }

    @GetMapping("/postDelete")
    public String postDelete(long postId){
        usedService.delete(postId);
        return "redirect:/used/list";
    }








}
