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

    @PostMapping("/openRoom") // 채팅방생성
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String openRoom(long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        long roomId = chattingService.openRoom(postId, principalDetails, "used"); // 채팅방 생성
        return "redirect:/chatDetail?roomId="+roomId+"&type=used"; // 채팅방 입장
    }

    @GetMapping("/chatRoomList") // 채팅방목록보기
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatRoomList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
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

    @GetMapping("/chatDetail") // 채팅방입장
    @PreAuthorize("hasRole('ROLE_GUEST') or hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String chatDetail(long roomId, String type, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
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

    @MessageMapping("/message") // 메시지전송
    public void messageRoom(Chat chat, Principal principal) throws InterruptedException {
        Thread.sleep(500);
        chattingService.sendMessage(chat, Long.parseLong(principal.getName()));
    }

    @PostMapping("/chatOut") // 채팅방나가기(중고, 모임 구분)
    public ResponseEntity<Void> chatOut(Chat chat, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if (chat.getType().equals("club")) { // 동네모임 채팅이면 모임 탈퇴까지 같이 함
            ClubResponse club = clubService.findClub(chat.getPostId(),principalDetails);
            if (clubService.deleteClubJoin(club, principalDetails) == 1) {
                clubService.updateNowMan(0, club.getClubId());
            }
        }
        chattingService.chatOut(chat, principalDetails.getMember().getMemberId()); // 채팅방 나가기
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deleteChatRoom") // 채팅방삭제(모임만 가능)
    public ResponseEntity<Void> deleteChatRoom(Chat chat) {
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

    @PostMapping("/chatKick") // 채팅추방(모임만 가능)
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

    @GetMapping("/chatNotification") // 네비바알림d
    @ResponseBody
    public boolean chatNotification(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        boolean result = false;

        if (principalDetails != null) {
            result = chattingService.chatNotification(principalDetails.getMember().getMemberId());
        }
        return result;
    }

    @PostMapping("/chatNotificationRemove") // 알림삭제
    public ResponseEntity<Void> chatNotificationRemove(long roomId, long memberId) {
        chattingService.chatNotificationRemove(roomId, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
