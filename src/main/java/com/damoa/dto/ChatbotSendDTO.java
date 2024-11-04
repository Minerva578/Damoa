package com.damoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotSendDTO {
    private String version;       // 예: "v2"
    private String userId;       // 사용자 고유 ID
    private String userIp;       // 사용자 IP 주소
    private String timestamp;       // 타임스탬프
    private List<Bubble> bubbles; // 버블 리스트
    private String event;         // 이벤트 타입

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bubble {
        private String type;       // 예: "text"
        private BubbleData data;   // 버블 데이터
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BubbleData {
        private String description; // 설명
    }
}
