package com.anabada.neighbor.chat.controller;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.service.ChattingService;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    @PostMapping("/openRoom")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String openRoom(long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        long roomId = chattingService.openRoom(postId, principalDetails, "used");
        return "redirect:/chatDetail?roomId="+roomId+"&type=used";
    }

    @GetMapping("/chatRoomList")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatRoomList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        model.addAttribute("list", chattingService.chattingRoomList(principalDetails));
        model.addAttribute("receiver", principalDetails.getMember().getMemberId());
        return "chatEx/chatRoomListPopup";
    }

    @GetMapping("/chatDetail")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatDetail(long roomId, String type, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        boolean check = chattingService.check(roomId, principalDetails);
        if (check) {
            model.addAttribute("list", chattingService.chattingMessageList(roomId, principalDetails, type));
            if (type.equals("used")) {
                model.addAttribute("receiver", chattingService.getReceiver(roomId, principalDetails));
            }
        }
        return type.equals("used") ? "chatEx/chatDetailPopupUsed" : "chatEx/chatDetailPopupClub";
    }

    @MessageMapping("/message")
    public void messageRoom(Chat chat, Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        chattingService.sendMessage(chat, principal);
    }

}
