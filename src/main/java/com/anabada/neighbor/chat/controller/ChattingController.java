package com.anabada.neighbor.chat.controller;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.service.ChattingService;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.file.service.FileUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;
    private final ClubService clubService;
    private final FileUtils fileUtils;

    @PostMapping("/openRoom")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')") // 채팅하기 버튼 클릭 시
    public String openRoom(long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        long roomId = chattingService.openRoom(postId, principalDetails, "used"); // 채팅방 생성
        return "redirect:/chatDetail?roomId="+roomId+"&type=used"; // 채팅방 입장
    }

    @GetMapping("/chatRoomList")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatRoomList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) { // 채팅방 목록 보기
        List<Chat> chatList = chattingService.chattingRoomList(principalDetails);

        if (chatList != null) {
            for (Chat chat : chatList) {
                if (chat.getFileResponseList() != null) {
                    List<FileInfo> fileInfoList = fileUtils.getFileInfo(chat.getFileResponseList());
                    chat.setFileInfo(fileInfoList.get(0));
                }
            }
        }

        model.addAttribute("list", chatList);

        return "chat/chatRoomListPopup";
    }

    @GetMapping("/chatDetail")
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatDetail(long roomId, String type, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) { //채팅방 입장
        boolean check = chattingService.check(roomId, principalDetails); // 입장 권한 체크
        if (check) {
            List<Chat> chatList = chattingService.chattingMessageList(roomId, principalDetails, type);

            if (chatList != null) {
                for (Chat chat : chatList) {
                    List<FileInfo> fileInfoList = fileUtils.getFileInfo(chat.getFileResponseList());
                    chat.setFileInfo(fileInfoList.get(0));
                }
            }

            model.addAttribute("list", chatList); // 메시지 리스트
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
        chattingService.chatOut(chat, principalDetails.getMember().getMemberId()); // 채팅방 나가기
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deleteChatRoom")
    public ResponseEntity<Void> deleteChatRoom(Chat chat) { // 채팅방 삭제(동네모임 채팅방만 가능)
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
    public ResponseEntity<Void> chatKick(Chat chat, long memberId) {
        chattingService.chatOut(chat, memberId);

        PrincipalDetails principalDetailsTemp = new PrincipalDetails(Member.builder()
                .memberId(memberId)
                .build());
        ClubResponse club = clubService.findClub(chat.getPostId(),principalDetailsTemp);
        if (clubService.deleteClubJoin(club, principalDetailsTemp) == 1) {
            clubService.updateNowMan(0, club.getClubId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chatNotification")
    @ResponseBody
    public boolean chatNotification(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        boolean result = false;

        if (principalDetails != null) {
            result = chattingService.chatNotification(principalDetails.getMember().getMemberId());
        }
        return result;
    }

    @PostMapping("/chatNotificationRemove")
    public ResponseEntity<Void> chatNotificationRemove(long roomId, long memberId) {
        chattingService.chatNotificationRemove(roomId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
