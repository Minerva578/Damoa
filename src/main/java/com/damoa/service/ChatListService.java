package com.damoa.service;

import com.damoa.component.ChatListAdapter;
import com.damoa.dto.chat.ChatListDTO;
import com.damoa.repository.interfaces.ChatListRepository;
import com.damoa.repository.model.ChatList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatListService {

    private final ChatListRepository chatListRepository;
    private final ChatListAdapter chatListAdapter;

    // senderId, receiverId 저장 기능
    public void saveByChatList(int senderId, int receiverId) {
        boolean exists = chatListRepository.existsBySenderIdAndReceiverId(senderId, receiverId);

        if (!exists) {
            ChatList chatList = new ChatList();
            chatList.setSenderId(senderId);
            chatList.setReceiverId(receiverId);
            chatListRepository.saveByChatList(chatList);
        }
    }

    /*
    * 채팅 목록을 찾아주는 기능
    * @param User sessionId
    * */
    public List<ChatListDTO> findByChatList(Integer sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("로그인 후 사용하세요.");  // 예외 메시지 개선
        }

        // 1. userId와 관련된 채팅 목록을 가져옴
        List<ChatList> chatLists = chatListRepository.findByChatList(sessionId);

        // 2. Stream 및 adapter 패턴 사용하여 ChatListDTO로 변환
        return chatLists.stream()
                .map(chatList -> chatListAdapter.adaptToDTO(chatList, sessionId))
                .collect(Collectors.toList());
    }

    // 채팅 목록 삭제 기능
    public void deleteByChatList(int roomId) {
        chatListRepository.deleteByChatList(roomId);
    }
}
