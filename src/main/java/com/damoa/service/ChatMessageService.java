package com.damoa.service;

import com.damoa.repository.interfaces.ChatMessageRepository;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    /**
     * 클라이언트가 WebSocket 연결 시 처리하는 메서드
     * @param message 클라이언트가 보낸 채팅 메시지
     * @return 저장된 메시지
     */
    public void saveMessage(ChatMessage message) {

        String roomId = message.getId();
        String senderId = message.getSenderId();
        String receiverId = message.getReceiverId();

        message.setId(roomId);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);

        // 기본 UTC -> KST 시간으로 타임스탬프 생성
        OffsetDateTime kstDateTime = OffsetDateTime.now(ZoneOffset.ofHours(9));
        String formattedTimestamp = kstDateTime.toString();

        message.setTimestamp(formattedTimestamp);

        log.info("메세지 내용: {}", message);
        log.info("발신자: {}", senderId);
        log.info("수신자: {}", receiverId);

        // 생성된 메시지를 DB에 저장
        chatMessageRepository.save(message);
    }

    // 채팅방 채팅 기록 조회 기능
    public List<ChatMessage> findByMessageList(String roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }
}
