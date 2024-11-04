package com.damoa.dto;

import java.util.List;

public class ChatbotRequestDTO {
    private String version;
    private String userId;
    private String userIp;
    private long timestamp;
    private List<Bubble> bubbles;
    private String event;

    // Getter and Setter methods
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Bubble> getBubbles() {
        return bubbles;
    }

    public void setBubbles(List<Bubble> bubbles) {
        this.bubbles = bubbles;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    // Inner class for Bubble
    public static class Bubble {
        private String type;
        private Data data;

        // Getter and Setter methods
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    // Inner class for Data
    public static class Data {
        private String description;

        // Getter and Setter methods
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
