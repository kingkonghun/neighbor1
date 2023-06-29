package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMember;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import com.anabada.neighbor.chat.repository.ChattingRepository;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
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
    private Map<String, Integer> chatNotificationMap = new HashMap<>(); // 새로운 채팅 갯수
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public long openRoom(long postId, PrincipalDetails principalDetails, String type) {
        long memberId = principalDetails.getMember().getMemberId();

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .postId(postId)
                .creator(memberId)
                .build();

        String memberName = principalDetails.getMember().getMemberName();

        if (type.equals("used")) {
            ChattingRoom chattingRoomTemp = chattingRepository.roomCheck(chattingRoom);

            if (chattingRoomTemp == null) {
                chattingRepository.insertRoom(chattingRoom);

                Post post = usedRepository.findPost(chattingRoom.getPostId());
                Member member = memberRepository.findByMemberId(post.getMemberId());
                Product product = usedRepository.findProduct(post.getPostId());

                long roomId = chattingRoom.getRoomId();
                long receiver = member.getMemberId();

                Chat chat = Chat.builder()
                        .postId(postId)
                        .title(post.getTitle())
                        .price(product.getPrice())
                        .roomId(roomId)
                        .sender(memberId)
                        .senderName(memberName)
                        .receiver(receiver)
                        .receiverName(memberRepository.findMemberName(receiver))
                        .content(memberName + "님이 입장하셨습니다.")
                        .messageDate(dateFormat.format(new Date()))
                        .messageType("ENTER")
                        .build();
                chattingRepository.insertMessage(chat);

                chattingRepository.insertChatMember(roomId, memberId);
                chattingRepository.insertChatMember(roomId, receiver);

                simpMessagingTemplate.convertAndSendToUser(String.valueOf(chat.getReceiver()), "/topic/messageNotification", chat);
                simpMessagingTemplate.convertAndSend("/topic/message/" + chat.getRoomId(), chat);
                return roomId;
            }else {
                return chattingRoomTemp.getRoomId();
            }
        }else {
            return 0;
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
            ChattingRoom chattingRoom = chattingRepository.findChatRoomByRoomId(message.getRoomId());
            Post post = usedRepository.findPost(chattingRoom.getPostId());
            Product product = usedRepository.findProduct(post.getPostId());
            Chat chat = Chat.builder()
                    .postId(post.getPostId())
                    .master(post.getMemberId())
                    .title(post.getTitle())
                    .price(product.getPrice())
                    .roomId(message.getRoomId())
                    .sender(message.getWriter())
                    .senderName(memberRepository.findMemberName(message.getWriter()))
                    .receiver(memberId)
                    .receiverName(memberRepository.findMemberName(memberId))
                    .content(message.getContent())
                    .messageDate(dateFormat.format(message.getMessageDate()))
                    .messageType(message.getMessageType())
                    .productStatus(product.getProductStatus())
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

    @Override
    public boolean check(long roomId, PrincipalDetails principalDetails) {

        int result = chattingRepository.check(ChattingMember.builder()
                .roomId(roomId)
                .memberId(principalDetails.getMember().getMemberId())
                .build());


        return result > 0 ? true : false;
    }

}
