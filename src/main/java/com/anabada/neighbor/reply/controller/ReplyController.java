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

@RequiredArgsConstructor
@RequestMapping("/reply")
@Controller
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/list")
    public String reply(long postId, Model model) {
        model.addAttribute("list", replyService.list(postId));
        return "/reply/list";
    }


    @ResponseBody
    @PostMapping("/write")
    public int write(Reply reply, HttpSession session) {
        replyService.write(reply, session);
        return 0;
    }

    @ResponseBody
    @PostMapping("/delete")
    public int delete(long replyId) {
        replyService.delete(replyId);
        return 0;
    }

    @ResponseBody
    @PostMapping("/update")
    public int update(Reply reply) {
        replyService.update(reply);
        return 0;
    }

    @ResponseBody
    @PostMapping("/writeReReply")
    public int writeReReply(Reply reply, HttpSession session) {
        replyService.writeReReply(reply, session);
        return 0;
    }

}
