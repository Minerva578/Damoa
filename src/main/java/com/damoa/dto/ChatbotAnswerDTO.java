package com.damoa.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatbotAnswerDTO {
    private String version;
    private String userId;
    private long timestamp;
    private List<Bubble> bubbles;
    private Scenario scenario;
    private List<Entity> entities;
    private List<Keyword> keywords;
    private Conversation conversation;
    private String normalizer;
    private String event;

    @Data
    public static class Bubble {
        private String type;
        private DataContent data;
        private List<Information> information;
        private List<Context> context;

        @Data
        public static class DataContent {
            private String description;
            private String url;
            private String urlAlias;
        }

        @Data
        public static class Information {
            private String key;
            private String value;
        }

        @Data
        public static class Context {
            // 필요한 필드를 추가하세요
        }
    }

    @Data
    public static class Scenario {
        private String name;
        private long chatUtteranceSetId;
        private List<Intent> intent;

        @Data
        public static class Intent {
            // 필요한 필드를 추가하세요
        }
    }

    @Data
    public static class Entity {
        // 필요한 필드를 추가하세요
    }

    @Data
    public static class Keyword {
        // 필요한 필드를 추가하세요
    }

    @Data
    public static class Conversation {
        private String scenarioName;
        private long chatUtteranceSetId;
        private List<Type> types;

        @Data
        public static class Type {
            // 필요한 필드를 추가하세요
        }
    }
}
