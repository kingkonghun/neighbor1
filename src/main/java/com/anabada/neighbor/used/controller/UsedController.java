package com.anabada.neighbor.used.controller;

import com.anabada.neighbor.used.domain.Img;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import com.anabada.neighbor.used.service.UsedService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
    @GetMapping("/findImg")
    public void findImg(long postId, HttpServletResponse response) throws IOException{
        String filenames = usedService.images(postId);
        usedService.downloadFiles(filenames,response);

    }







}
