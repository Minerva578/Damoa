package com.damoa.repository.model;

import com.damoa.dto.user.ProjectListDTO;
import com.damoa.service.SkillService;
import com.damoa.service.UserService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Autowired
    private UserService userService;

    @Autowired
    private SkillService skillService;

    private int id;
    private int userId;
    private String job;
    private String skill;
    private String projectName;
    private int minYears;
    private int maxYears;
    private Date projectStart;
    private String expectedPeriod;
    private int period;
    private String salaryType;
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
    private byte[] files; // Blob 타입을 byte 배열로
    private String company;
    private String managerName;
    private String contact;
    private String email;
    private Timestamp createdAt;
    private int majorStatus;
    private int minorStatus;


}
