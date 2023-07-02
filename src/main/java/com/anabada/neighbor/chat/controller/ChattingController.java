package com.anabada.neighbor.chat.controller;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.service.ChattingService;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") // 채팅하기 버튼 클릭 시
    public String openRoom(long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        long roomId = chattingService.openRoom(postId, principalDetails, "used"); // 채팅방 생성
        return "redirect:/chatDetail?roomId="+roomId+"&type=used"; // 채팅방 입장
    }

    @GetMapping("/chatRoomList")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatRoomList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 채팅방 목록 보기
        model.addAttribute("list", chattingService.chattingRoomList(principalDetails));
        return "chat/chatRoomListPopup";
    }

    @GetMapping("/chatDetail")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatDetail(long roomId, String type, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 채팅방 입장
        boolean check = chattingService.check(roomId, principalDetails); // 입장 권한 체크
        if (check) {
            model.addAttribute("list", chattingService.chattingMessageList(roomId, principalDetails, type)); // 메시지 리스트
            model.addAttribute("memberList", chattingService.chattingMemberList(roomId));
            if (type.equals("used")) {
                model.addAttribute("receiver", chattingService.getReceiver(roomId, principalDetails));
            }
        }
        return type.equals("used") ? "chat/chatDetailPopupUsed" : "chat/chatDetailPopupClub";
    }

    @MessageMapping("/message")
    public void messageRoom(Chat chat, Principal principal) throws InterruptedException { // 메시지 전송
        Thread.sleep(500);
        chattingService.sendMessage(chat, principal);
    }

    @PostMapping("/chatOut")
    public ResponseEntity<Void> chatOut(long roomId, String type, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 채팅방 나가기
        chattingService.chatOut(roomId, type, principalDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
