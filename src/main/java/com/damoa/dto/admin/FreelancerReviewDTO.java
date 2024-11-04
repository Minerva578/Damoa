package com.damoa.dto.admin;


import lombok.Data;

@Data
public class FreelancerReviewDTO {

    private int id;
    private String username;
    private String projectName;
    private int overallScore;
    private String responseData;

}
