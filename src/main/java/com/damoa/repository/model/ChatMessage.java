package com.damoa.repository.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// MongoDB Document 모델
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages") // DB 컬렉션 네임
public class ChatMessage {
    @Id
    private String id;
    private String roomId;
    private String senderId;
    private String receiverId;
    private String message;
    private String timestamp;

}
