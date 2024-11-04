package com.damoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String userId;     // 사용자 ID
    private long timestamp;    // 타임스탬프
    private String message;     // 메시지 내용
    private String event;       // 이벤트 종류
}
