package com.damoa.dto.admin;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NoticeDTO {

    private int id;
    private int type;
    private String title;
    private String content;
    private Timestamp createdAt;
}
