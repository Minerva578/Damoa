package com.damoa.repository.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWait {

    private int id;
    private int userId;
    private int projectId;
    private String introduce;
    private long minimumWage;
    private long maximumWage;
    private LocalDate startDate;
    private String originFileName;
    private String uploadFileName;
    private String detail;
    private int status;
    private Timestamp createdAt;
}
