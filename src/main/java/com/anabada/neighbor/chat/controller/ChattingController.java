package com.anabada.neighbor.chat.controller;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.service.ChattingService;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;
    private final ClubService clubService;

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
        chattingService.sendMessage(chat, Long.parseLong(principal.getName()));
    }

    @PostMapping("/chatOut")
    public ResponseEntity<Void> chatOut(Chat chat, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 채팅방 나가기

        if (chat.getType().equals("club")) { // 동네모임 채팅이면 모임 탈퇴까지 같이 함
            ClubResponse club = clubService.findClub(chat.getPostId(),principalDetails);
            if (clubService.deleteClubJoin(club, principalDetails) == 1) {
                clubService.updateNowMan(0, club.getClubId());
            }
        }
        chattingService.chatOut(chat, principalDetails); // 채팅방 나가기
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deleteChatRoom")
    public ResponseEntity<Void> deleteChatRoom(Chat chat) { // 채팅방 삭제(동네모임 채팅방만 가능)
        System.out.println("삭제 입장");
        long roomId = chat.getRoomId();
        long postId = chat.getPostId();

        List<Member> memberList = chattingService.chattingMemberList(roomId); // 채팅방에 있는 모든 사용자를 조회

        for (Member member : memberList) {
            PrincipalDetails principalDetailsTemp = new PrincipalDetails(member);
            ClubResponse club = clubService.findClub(postId, principalDetailsTemp);
            if (clubService.deleteClubJoin(club, principalDetailsTemp) == 1) { // 모임 가입 사용자 전부 탈퇴처리
                clubService.updateNowMan(0, club.getClubId());
            }
        }
        chattingService.deleteChatRoom(roomId);
        clubService.deletePost(postId); // 해당 게시물까지 같이 삭제

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/chatKick")
    public ResponseEntity<Void> chatKick(long memberId) {
        System.out.println(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
