package com.damoa.dto.admin;

import lombok.Data;

import java.util.Date;

@Data
public class FreelancerReviewDetailDTO {

    private int id;
    private int companyId;
    private int freelancerId;
    private int communicationScore;
    private int timelinessScore;
    private int supportScore;
    private int willingnessScore;
    private int overallScore;
    private Date reviewDate;
    private String responseData;


}
