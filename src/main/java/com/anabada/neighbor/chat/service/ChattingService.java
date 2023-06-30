package com.anabada.neighbor.chat.service;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.config.auth.PrincipalDetails;

import java.security.Principal;
import java.util.List;

public interface ChattingService {
    long openRoom(long postId, PrincipalDetails principalDetails, String type);

    void sendMessage(Chat chat, Principal principal);

    List<Chat> chattingRoomList(PrincipalDetails principalDetails);

    List<Chat> chattingMessageList(long roomId, PrincipalDetails principalDetails);

    Chat getReceiver(long roomId, PrincipalDetails principalDetails);

    boolean check(long roomId, PrincipalDetails principalDetails);
}
