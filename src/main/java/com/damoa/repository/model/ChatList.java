package com.damoa.repository.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatList {
    private int id;
    private int senderId;
    private int receiverId;
    private Timestamp createdAt;
    private boolean visibleToSender;
    private boolean visibleToReceiver;
}
