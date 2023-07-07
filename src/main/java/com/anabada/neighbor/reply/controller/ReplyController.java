package com.anabada.neighbor.reply.controller;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.page.PageDTO;
import com.anabada.neighbor.reply.domain.Reply;
import com.anabada.neighbor.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/list") // 댓글목록
    public String reply(long postId, Model model) {
        model.addAttribute("list", replyService.list(postId));
        System.out.println("replyList : "+replyService.list(postId));
        return "/reply/list";
    }

    @PostMapping("/write") // 댓글작성
    public ResponseEntity<Void> write(Reply reply, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        replyService.write(reply, principalDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/delete") // 댓글삭제
    public ResponseEntity<Void> delete(long replyId) {
        replyService.delete(replyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update") // 댓글수정
    public ResponseEntity<Void> update(Reply reply) {
        replyService.update(reply);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/writeReReply") // 대댓글작성
    public ResponseEntity<Void> writeReReply(Reply reply, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        replyService.writeReReply(reply, principalDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/myReply") // 내가 쓴 댓글
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String myReply(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, Criteria criteria){
        model.addAttribute("list",replyService.findMyReply(principalDetails.getMember().getMemberId(),criteria));
        int total = replyService.countMyReply(principalDetails.getMember().getMemberId());
        model.addAttribute("pageMaker", new PageDTO(total, 10, criteria));
        return "reply/myReply";
    }
}
