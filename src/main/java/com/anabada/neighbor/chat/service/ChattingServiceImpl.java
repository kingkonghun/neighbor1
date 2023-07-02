package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.domain.ChattingMember;
import com.anabada.neighbor.chat.domain.ChattingMessage;
import com.anabada.neighbor.chat.domain.ChattingRoom;
import com.anabada.neighbor.chat.repository.ChattingRepository;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.repository.ClubRepository;
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
    private final ClubRepository clubRepository;
    private Map<String, Integer> chatNotificationMap = new HashMap<>(); // 새로운 채팅 갯수
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 채팅방 만들기
     */
    @Override
    public long openRoom(long postId, PrincipalDetails principalDetails, String type) {

        long memberId = principalDetails.getMember().getMemberId();
        String memberName = principalDetails.getMember().getMemberName();

        ChattingRoom chattingRoom = ChattingRoom.builder() // 채팅방 생성시 매개변수로 전달할 객체 생성
                .postId(postId)
                .creator(memberId)
                .type(type)
                .build();

        ChattingRoom chattingRoomTemp = chattingRepository.roomCheck(chattingRoom); // 방이 이미 존재하는지 확인

        Chat chat = null;
        if (chattingRoomTemp == null) { // 방이 존재하지 않는다면
            chattingRepository.insertRoom(chattingRoom); // 채팅방을 생성(생성 직후 roomId를 chattingRoom 에 담아옴)
            long roomId = chattingRoom.getRoomId();

            if (type.equals("used")) { // 중고거래 채팅이라면
                Post post = usedRepository.findPost(chattingRoom.getPostId()); // postId 로 게시물의 정보를 가져옴
                Product product = usedRepository.findProduct(post.getPostId()); // postId 로 상품의 정보를 가져옴
                Member member = memberRepository.findByMemberId(post.getMemberId()); // memberId 로 사용자 정보를 가져옴

                long receiver = member.getMemberId(); // 게시물 주인의 memberId 를 가져옴

                String receiverName = memberRepository.findMemberName(receiver); // 게시물 주인의 memberName 을 가져옴

                chattingRepository.insertChatMember(roomId, memberId); // 채팅방 참가자 생성
                chattingRepository.insertChatMember(roomId, receiver); // 채팅방 참가자 생성

                chat = Chat.builder()
                        .postId(postId)
                        .title(post.getTitle())
                        .price(product.getPrice())
                        .roomId(roomId)
                        .sender(receiver)
                        .senderName(memberName)
                        .receiver(receiver)
                        .receiverName(receiverName)
                        .content("x")
                        .messageDate(dateFormat.format(new Date()))
                        .messageType("LINE")
                        .type("used")
                        .build();
                chattingRepository.insertMessage(chat); // 상대방의 채팅 시작점 만들기

                chat.setSender(memberId);
                chattingRepository.insertMessage(chat); // 나의 채팅 시작점 만들기

                chat.setSender(receiver);
                chat.setContent(receiverName + "님이 입장하셨습니다.");
                chat.setMessageType("ENTER");
                chattingRepository.insertMessage(chat); // 상대방의 입장 메시지 등록

                chat.setSender(memberId);
                chat.setContent(memberName + "님이 입장하셨습니다.");
                chat.setMessageType("ENTER");
                chattingRepository.insertMessage(chat); // 나의 입장 메시지 등록

                simpMessagingTemplate.convertAndSend("/topic/message/" + chat.getRoomId(), chat); // 방에 메시지 보내기

                simpMessagingTemplate.convertAndSendToUser(String.valueOf(chat.getReceiver()), "/topic/messageNotification", chat); // 상대방에게 개인 알림 보내기

                return roomId; // 생성한 채팅방의 roomId 를 리턴
            } else if (type.equals("club")) { // 동네모임 채팅이라면
                chat = Chat.builder()
                        .postId(postId)
                        .roomId(roomId)
                        .sender(memberId)
                        .senderName(memberName)
                        .content("x")
                        .messageDate(dateFormat.format(new Date()))
                        .messageType("LINE")
                        .build();

                chattingRepository.insertChatMember(roomId, memberId); // 채팅방 참가자 생성

                chattingRepository.insertMessage(chat); // 나의 채팅 시작점 만들기

                chat.setContent(memberName + "님 환영합니다.");
                chat.setMessageType("ENTER");
                chattingRepository.insertMessage(chat); // 나의 환영 메시지 등록
            }
        }else { // 방이 이미 존재한다면
            chattingRepository.updateStatus(chattingRoomTemp.getRoomId()); // used 일 때만 실행되야 할 수도 있음(수정예정)
            return chattingRoomTemp.getRoomId(); // 이미 존재하는 방의 roomId 를 리턴
        }
        return 0;
    }

    /**
     * 메시지 보내기
     */
    @Override
    public void sendMessage(Chat chat, Principal principal) {

        chat.setSender(Long.parseLong(principal.getName()));
        chat.setSenderName(memberRepository.findMemberName(chat.getSender()));

        chattingRepository.insertMessage(chat); // 메시지 DB 에 insert

        chat.setMessageDate(dateFormat.format(new Date())); // 현재 날짜 생성 (DB 에는 default 로 현재 날짜가 생성되지만, 메시지 보낼 용도로 생성)

        List<Long> chatMemberIdList = chattingRepository.findChatMemberIdByRoomId(chat.getRoomId()); // roomId 로 해당 채팅방에 있는 memberId 를 전부 가져옴
        for (long chatMemberId : chatMemberIdList) {
            String key = chat.getRoomId() + "_" + chatMemberId; // map 을 조회할 key

            if (Long.parseLong(principal.getName()) == chatMemberId) { // chatMemberId 가 본인이면
                chatNotificationMap.remove(key); // 저장된 알림(ex : 1, 2, 3, 4) 을 지움
                continue;
            }

            if (chatNotificationMap.get(key) == null || chatNotificationMap.get(key) == 0) { // value 가 null, 0 이면
                chatNotificationMap.put(key, 1); // value 를 1 로 update
            } else { // 이미 value 가 1 이상이라면
                chatNotificationMap.put(key, chatNotificationMap.get(key) + 1); // value 에 +1 을 함
            }

            simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatMemberId), "/topic/messageNotification", chat); // 채팅방에 입장중인 member(본인을 제외한) 에게 개인 알림 보내기
        }
        simpMessagingTemplate.convertAndSend("/topic/message/" + chat.getRoomId(), chat); // 해당 채팅방에 메시지 보내기
        chattingRepository.updateStatus(chat.getRoomId()); // used 일 때만 실행되야 할 수도 있음(수정예정)
    }

    /**
     * 채팅방 목록
     */
    @Override
    public List<Chat> chattingRoomList(PrincipalDetails principalDetails) {
        List<Chat> chatList = new ArrayList<>(); // 채턴할 객체 생성

        long memberId = principalDetails.getMember().getMemberId();

        List<Long> roomIdList = chattingRepository.findRoomIdByMemberId(memberId); // 내 memberId 로 채팅방을 조회해서 roomId 의 list 를 가져옴
        for (long roomId : roomIdList) {

            String type = chattingRepository.findTypeByRoomId(roomId); // 해당 채팅방의 type 을 가져옴( ex) "used", "club")
            String lastMessage = chattingRepository.lastMessage(roomId); // 해당 채팅방의 마지막 메시지를 가져옴
            
            Chat chat = null;

            List<Long> memberIdList = chattingRepository.findChatMemberIdByRoomId(roomId); // 채팅방에 등록된 사용지의 memberId 를 가져옴
            String chatMemberStatus = chattingRepository.chatMemberStatus(roomId, memberId); // 해당 사용자가 채팅방을 나간 사용자인지 확인하는 상태값을 가져옴

            if (type.equals("used")) { // 중고거래 채팅이면
                long sender = memberIdList.get(0); 
                if (memberId == sender) { // sender 에 내가 아닌 사용자의 memberId 를 넣기 위한 처리
                    sender = memberIdList.get(1);
                }

                chat = Chat.builder()
                        .roomId(roomId)
                        .receiver(memberId)
                        .sender(sender)
                        .content(lastMessage)
                        .chatCount(chatNotificationMap.get(roomId + "_" + principalDetails.getMember().getMemberId()) != null ? chatNotificationMap.get(roomId + "_" + principalDetails.getMember().getMemberId()) : 0)
                        .type(type)
                        .chatMemberStatus(chatMemberStatus)
                        .build();
                chat.setSenderName(memberRepository.findMemberName(chat.getSender()));
            } else if (type.equals("club")) { // 동네모임 채팅이면

                ChattingRoom chattingRoom = chattingRepository.findChatRoomByRoomId(roomId); // 해당 채팅방의 정보를 가져옴
                Post post = clubRepository.selectPost(chattingRoom.getPostId()); // postId 로 게시물의 정보를 가져옴

                chat = Chat.builder()
                        .roomId(roomId)
                        .postId(post.getPostId())
                        .title(post.getTitle())
                        .memberList(memberIdList)
                        .receiver(memberId)
                        .content(lastMessage)
                        .chatCount(chatNotificationMap.get(roomId + "_" + principalDetails.getMember().getMemberId()) != null ? chatNotificationMap.get(roomId + "_" + principalDetails.getMember().getMemberId()) : 0)
                        .type(type)
                        .build();
            }
            chatList.add(chat); // 리턴할 List 에 저장
        }
        return chatList;
    }

    /**
     * 채팅 메시지 목록
     */
    @Override
    public List<Chat> chattingMessageList(long roomId, PrincipalDetails principalDetails, String type) {
        long memberId = principalDetails.getMember().getMemberId();
        String key = roomId + "_" + memberId; // map 을 조회할 key
        chatNotificationMap.remove(key); // 해당 채팅방에 있는 알림 제거

        long lineMessageId = chattingRepository.findLineMessageId(roomId, memberId); // 내 채팅 시작점 가져오기

        List<Chat> chatList = new ArrayList<>();
        
        List<ChattingMessage> messageList = chattingRepository.chattingMessageList(roomId, memberId, lineMessageId); // 시작점 부터 메시지를 전부 가져오기

        for (ChattingMessage message : messageList) {
            ChattingRoom chattingRoom = chattingRepository.findChatRoomByRoomId(message.getRoomId()); // 채팅방의 정보를 가져옴
            
            Chat chat = null;
            if (type.equals("used")) { // 중고거래 채팅이면
                Post post = usedRepository.findPost(chattingRoom.getPostId()); // postId 로 게시물의 정보를 가져옴
                Product product = usedRepository.findProduct(post.getPostId()); // postId 로 상품의 정보를 가져옴
                chat = Chat.builder()
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
                        .type(type)
                        .build();
            } else if (type.equals("club")) { // 동네모임 채팅이면
                Post post = clubRepository.selectPost(chattingRoom.getPostId()); // postId 로 게시물의 정보를 가져옴
                Club club = clubRepository.selectClub(post.getPostId()); // postId 로 모임의 정보를 가져옴

                chat = Chat.builder()
                        .postId(post.getPostId())
                        .master(post.getMemberId())
                        .title(post.getTitle())
                        .roomId(message.getRoomId())
                        .sender(message.getWriter())
                        .senderName(memberRepository.findMemberName(message.getWriter()))
                        .receiver(memberId)
                        .receiverName(memberRepository.findMemberName(memberId))
                        .content(message.getContent())
                        .messageDate(dateFormat.format(message.getMessageDate()))
                        .messageType(message.getMessageType())
                        .memberCount(chattingRepository.chatMemberCount(message.getRoomId()) - 1)
                        .hobbyName(clubRepository.selectHobbyName(club.getHobbyId()))
                        .nowMan(club.getNowMan())
                        .maxMan(club.getMaxMan())
                        .type(type)
                        .build();
            }
            chatList.add(chat); // 리턴할 List 에 저장

        }
        return chatList;
    }

    /**
     * 1대1 채팅 상대방 정보(used)
     */
    @Override
    public Chat getReceiver(long roomId, PrincipalDetails principalDetails) {
        List<Long> memberIdList = chattingRepository.findChatMemberIdByRoomId(roomId); // 해당 채팅방에 등록된 memberId 를 가져옴
        long memberId = principalDetails.getMember().getMemberId();
        
        long receiver = memberId == memberIdList.get(0) ? memberIdList.get(1) : memberIdList.get(0); // 내가 아닌 상대방을 구분하기 위함
        
        Chat chat = Chat.builder()
                .receiver(receiver)
                .receiverName(memberRepository.findMemberName(receiver))
                .build();
        return chat; // 채팅 상대방의 memberId 와 memberName 을 담은 객체
    }

    /**
     * 채팅방 입장 권한 체크
     */
    @Override
    public boolean check(long roomId, PrincipalDetails principalDetails) {

        int result = chattingRepository.check(ChattingMember.builder()
                .roomId(roomId)
                .memberId(principalDetails.getMember().getMemberId())
                .build()); // 해당 방의 참가자로 등록되어 있는지 확인하는 절차

        return result > 0 ? true : false;
    }

    /**
     * 채팅방 나가기
     */
    @Override
    public void chatOut(long roomId, String type, PrincipalDetails principalDetails) {
        long memberId = principalDetails.getMember().getMemberId();
        String memberName = principalDetails.getMember().getMemberName();
        String key = roomId + "_" + memberId; // map 을 조회할 key
        chatNotificationMap.remove(key); // 해당 채팅방의 내 알림 삭제

        Chat chat = Chat.builder()
                .roomId(roomId)
                .sender(memberId)
                .content("x")
                .messageType("LINE")
                .build(); // 나의 채팅 시작점을 재정의 

        chattingRepository.insertMessage(chat); // 나의 채팅 시작점을 재정의
        chattingRepository.chatOut(roomId, memberId); // chattingMember 에서 나의 상태를 n 으로 변경

        if (type.equals("club")) { // 동네모임 채팅이면
            chat.setSenderName(memberName);
            chat.setContent(memberName + "님이 퇴장하셨습니다.");
            chat.setMessageDate(dateFormat.format(new Date()));
            chat.setMessageType("ENTER"); 
            chattingRepository.insertMessage(chat); // 퇴장 메시지 저장
            simpMessagingTemplate.convertAndSend("/topic/message/" + roomId, chat); // 해당 방에 퇴장 메시지 보내기
        }
    }
}
