package com.damoa.dto;

import com.damoa.repository.model.Project;
import com.damoa.repository.model.Skill;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
public class ProjectSaveDTO {
    private int id;
    private int userId;
    private String job;
    private String skill;
    private List<String> totalSkills;
    private String projectName;
    private int minYears;
    private int maxYears;
    private LocalDate projectStart;
    private String expectedPeriod;
    private int period;
    private String salaryType;
    private String workingStyle;
    private String meeting;
    private String address;
    private String worktime;
    private String workAdjust;
    private String vacation;
    private byte[] progress; // Blob 타입을 byte 배열로
    private byte[] mainTasks; // Blob 타입을 byte 배열로
    private byte[] detailTask; // Blob 타입을 byte 배열로
    private byte[] delivered; // Blob 타입을 byte 배열로
    private MultipartFile files; // Blob 타입을 byte 배열로
    private String company;
    private String managerName;
    private String contact;
    private String email;
    private int agree;



}
