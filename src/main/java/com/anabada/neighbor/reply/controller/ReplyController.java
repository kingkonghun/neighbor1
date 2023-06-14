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

    @GetMapping("/list") //댓글 리스트
    public String reply(long postId, Model model) {
        model.addAttribute("list", replyService.list(postId));
        return "/reply/list";
    }


    @ResponseBody
    @PostMapping("/write") //댓글 작성
    public int write(Reply reply, HttpSession session) {
        replyService.write(reply, session);
        return 0;
    }

    @ResponseBody
    @PostMapping("/delete") //댓글 삭제
    public int delete(long replyId) {
        replyService.delete(replyId);
        return 0;
    }

    @ResponseBody
    @PostMapping("/update") //댓글 수정
    public int update(Reply reply) {
        replyService.update(reply);
        return 0;
    }

    @ResponseBody
    @PostMapping("/writeReReply") //대댓글 작성
    public int writeReReply(Reply reply, HttpSession session) {
        replyService.writeReReply(reply, session);
        return 0;
    }

}
