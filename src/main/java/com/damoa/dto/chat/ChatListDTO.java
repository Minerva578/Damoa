package com.damoa.dto.chat;

import com.damoa.repository.model.ChatList;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatListDTO {
    private String receiver;    // @param User.getUsername
    private ChatList chatList;
    private Integer sessionUserId;
}
