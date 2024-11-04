package com.damoa.dto.user;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectListDTO {
    private int id;
    private String userName;
    private String job;
    private List<String> skill;
    private String projectName;
    private String requireYears;
    private String projectStart;
    private String period;
    private String projectType;
    private String workingStyle;
    private String meeting;
    private String address;
    private String worktime;
    private String workAdjust;
    private String vacation;
    private String progress; // Blob 타입을 byte 배열로
    private String mainTasks; // Blob 타입을 byte 배열로
    private String detailTask; // Blob 타입을 byte 배열로
    private String delivered; // Blob 타입을 byte 배열로
    private String company;
    private String managerName;
    private String contact;
    private String email;
    private byte[] files; // Blob 타입을 byte 배열로
    private String createdAt;
    private Boolean isOwner;

}
