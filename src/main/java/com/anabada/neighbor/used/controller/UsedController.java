package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

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
    public String post(Used used, HttpSession session){
        long memberId = (long)session.getAttribute("memberId");
        used.setMemberId(memberId);
        usedService.write(used);
        return "redirect:/used/list";
    }


}
