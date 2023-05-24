package com.anabada.neighbor.reply.controller;

import com.anabada.neighbor.reply.domain.Reply;
import com.anabada.neighbor.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/reply")
@Controller
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/list")
    public String reply(long postId, Model model) {
        System.out.println(postId);
        model.addAttribute("list", replyService.list(postId));
        return "/reply/list";
    }


    @ResponseBody
    @PostMapping("write")
    public int write(Reply reply, Model model, HttpSession session) {
        reply.setMemberId((Long)session.getAttribute("memberId"));
        replyService.write(reply);
        return 0;
    }
}
