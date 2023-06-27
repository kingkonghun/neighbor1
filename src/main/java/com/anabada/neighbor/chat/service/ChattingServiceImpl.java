package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import com.anabada.neighbor.chat.repository.ChattingRepository;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.repository.UsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChattingRepository chattingRepository;
    private final MemberRepository memberRepository;
    private final UsedRepository usedRepository;
    private Map<String, Integer> chatNotificationMap = new HashMap<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public long openRoom(ChattingRoom chattingRoom, PrincipalDetails principalDetails) {

        chattingRoom.setCreator(principalDetails.getMember().getMemberId());

        ChattingRoom chattingRoomTemp = chattingRepository.roomCheck(chattingRoom);

        if (chattingRoomTemp == null) {
            chattingRepository.insertRoom(chattingRoom);

            long roomId = chattingRoom.getRoomId();

            Post post = usedRepository.findPost(chattingRoom.getPostId());
            Member member = memberRepository.findByMemberId(post.getMemberId());

            chattingRepository.insertChatMember(roomId, chattingRoom.getCreator());
            chattingRepository.insertChatMember(roomId, member.getMemberId());
            return roomId;
        }else {
            return chattingRoomTemp.getRoomId();
        }
    }

    @Override
    public void sendMessage(Chat chat, Principal principal) {


        chat.setSender(Long.parseLong(principal.getName()));
        chat.setSenderName(memberRepository.findMemberName(chat.getSender()));

        chat.setReceiverName(memberRepository.findMemberName(chat.getReceiver()));

        String key = chat.getRoomId() + "_" + chat.getReceiver();

        chattingRepository.insertMessage(chat);
        chat.setSenderName(memberRepository.findMemberName(chat.getSender()));
        chat.setMessageDate(dateFormat.format(new Date()));

        System.out.println(dateFormat.format(new Date()));
        if (chatNotificationMap.get(key) == null || chatNotificationMap.get(key) == 0) {
            chatNotificationMap.put(key, 1);
        } else {
            chatNotificationMap.put(key, chatNotificationMap.get(key) + 1);
        }

        simpMessagingTemplate.convertAndSendToUser(String.valueOf(chat.getReceiver()), "/topic/messageNotification", chat);
        simpMessagingTemplate.convertAndSend("/topic/message/" + chat.getRoomId(), chat);
    }

    @Override
    public List<Chat> chattingRoomList(PrincipalDetails principalDetails) {
        List<Chat> chatList = new ArrayList<>();

        long memberId = principalDetails.getMember().getMemberId();

        List<Long> roomIdList = chattingRepository.findRoomIdByMemberId(memberId);
        for (long roomId : roomIdList) {
            String lastMessage = chattingRepository.lastMessage(roomId);
            List<Long> memberIdList = chattingRepository.findChatMemberIdByRoomId(roomId);
            long sender = memberIdList.get(0);
            if (memberId == sender) {
                sender = memberIdList.get(1);
            }

            Chat chat = Chat.builder()
                    .roomId(roomId)
                    .receiver(memberId)
                    .sender(sender)
                    .content(lastMessage)
                    .chatCount(chatNotificationMap.get(roomId + "_" + principalDetails.getMember().getMemberId()) != null ? chatNotificationMap.get(roomId + "_" + principalDetails.getMember().getMemberId()) : 0)
                    .build();
            chat.setSenderName(memberRepository.findMemberName(chat.getSender()));
            chatList.add(chat);
        }
        return chatList;
    }

    @Override
    public List<Chat> chattingMessageList(long roomId, PrincipalDetails principalDetails) {
        long memberId = principalDetails.getMember().getMemberId();
        String key = roomId + "_" + memberId;
        chatNotificationMap.remove(key);
        List<Chat> chatList = new ArrayList<>();
        List<ChattingMessage> messageList = chattingRepository.chattingMessageList(roomId);

        for (ChattingMessage message : messageList) {
            Chat chat = Chat.builder()
                    .roomId(message.getRoomId())
                    .sender(message.getWriter())
                    .senderName(memberRepository.findMemberName(message.getWriter()))
                    .receiver(memberId)
                    .receiverName(memberRepository.findMemberName(memberId))
                    .content(message.getContent())
                    .messageDate(dateFormat.format(message.getMessageDate()))
                    .build();
            chatList.add(chat);
        }
        return chatList;
    }

    @Override
    public Chat getReceiver(long roomId, PrincipalDetails principalDetails) {
        List<Long> memberIdList = chattingRepository.findChatMemberIdByRoomId(roomId);
        long memberId = principalDetails.getMember().getMemberId();
        long receiver = memberId == memberIdList.get(0) ? memberIdList.get(1) : memberIdList.get(0);
        Chat chat = Chat.builder()
                .receiver(receiver)
                .receiverName(memberRepository.findMemberName(receiver))
                .build();
        return chat;
    }

}
