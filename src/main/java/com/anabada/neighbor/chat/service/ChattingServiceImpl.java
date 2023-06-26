package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import com.anabada.neighbor.chat.repository.ChattingRepository;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChattingRepository chattingRepository;
    private final MemberRepository memberRepository;

    @Override
    public ChattingRoom openRoom(ChattingRoom chattingRoom, PrincipalDetails principalDetails) {

        chattingRoom.setSender(principalDetails.getMember().getMemberId());

        ChattingRoom chattingRoomTemp = chattingRepository.roomCheck(chattingRoom);

        if (chattingRoomTemp == null) {
            chattingRepository.insertRoom(chattingRoom);
            System.out.println("create roomId = " + chattingRoom.getRoomId());
            return chattingRoom;
        }else {
            System.out.println("NO! create roomId = " + chattingRoomTemp.getRoomId());
            return chattingRoomTemp;
        }
    }

    @Override
    public void sendMessage(Chat chat, Principal principal) {
        chat.setSender(Long.parseLong(principal.getName()));


        chattingRepository.insertMessage(chat);
        chat.setSenderName(memberRepository.findMemberName(chat.getSender()));

        simpMessagingTemplate.convertAndSendToUser(String.valueOf(chat.getReceiver()), "/topic/messageNotification", chat);
        simpMessagingTemplate.convertAndSend("/topic/message/" + chat.getRoomId(), chat);
    }

    @Override
    public List<Chat> chattingRoomList(PrincipalDetails principalDetails) {
        List<Chat> chatList = new ArrayList<>();

        long memberId = principalDetails.getMember().getMemberId();

        List<ChattingRoom> chattingRoomList = chattingRepository.chattingRoomList(memberId);

        for (ChattingRoom chattingRoom : chattingRoomList) {
            String lastMessage = chattingRepository.lastMessage(chattingRoom.getRoomId());
            Chat chat = Chat.builder()
                    .roomId(chattingRoom.getRoomId())
                    .receiver(memberId)
                    .sender(chattingRoom.getSender() == memberId ? chattingRoom.getReceiver() : chattingRoom.getSender())
                    .content(lastMessage)
                    .build();

            chat.setSenderName(memberRepository.findMemberName(chat.getSender()));
            chatList.add(chat);
        }
        return chatList;
    }

    @Override
    public List<ChattingMessage> chattingMessageList(long roomId) {

        return chattingRepository.chattingMessageList(roomId);
    }

    @Override
    public long getReceiver(long roomId, PrincipalDetails principalDetails) {
        ChattingRoom chattingRoom = chattingRepository.findChatRoomByRoomId(roomId);
        long receiver = chattingRoom.getReceiver();
        long sender = principalDetails.getMember().getMemberId();
        if (receiver != sender) {
            return receiver;
        }else {
            return chattingRoom.getSender();
        }
    }

}
