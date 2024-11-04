package com.damoa.dto.admin;


import lombok.Data;

@Data
public class CompanyReviewDTO {

    private int id;
    private String username;
    private String projectName;
    private String mainTasks;
    private int overallScore;
    private String responseData;

}
