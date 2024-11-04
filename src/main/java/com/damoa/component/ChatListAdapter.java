package com.damoa.component;

import com.damoa.dto.chat.ChatListDTO;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.ChatList;
import com.damoa.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatListAdapter {

    private final UserRepository userRepository;

    // 3. ChatListDTO로 변환하는 기능
    public ChatListDTO adaptToDTO(ChatList chatList, Integer sessionId) {
        int selectUserId = getCounterpartId(chatList, sessionId);
        User receiver = userRepository.findById(selectUserId);

        return new ChatListDTO(receiver.getUsername(), chatList, sessionId);
    }

    // 4. 목록에 표시할 상대방 아이디를 찾기 위한 데이터 검사 기능
    // TODO: 사용자 pk정보 및 이름 정보는 변경되지 않음으로 추후 캐싱을 해볼 수 있다!
    private int getCounterpartId(ChatList chatList, Integer userId) {

        return (userId == chatList.getSenderId()) ? chatList.getReceiverId() : chatList.getSenderId();
    }
}
