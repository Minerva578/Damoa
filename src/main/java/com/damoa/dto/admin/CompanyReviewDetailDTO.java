package com.damoa.dto.admin;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyReviewDetailDTO {

    private int id;
    private int companyId;
    private int freelancerId;
    private int workQualityScore;
    private int timelinessScore;
    private int feedbackScore;
    private int willingnessScore;
    private int overallScore;
    private Date reviewDate;
    private String responseData;

}
