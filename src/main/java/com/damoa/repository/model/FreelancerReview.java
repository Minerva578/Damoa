package com.damoa.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerReview {

    private Integer id;
    private int writerId;
    private int companyId;

    // 모든 평점은 별점 1~5점
    private int communicationScore;     // 커뮤니케이션 정도 모든 평점은 별점 1~5점
    private int timelinessScore;        // 계약 기간준수
    private int supportScore;           // 클라이언트 회사 내 개발환경 평가(상주라면)
    private int willingnessScore;       // 클라이언트와 함께하고 싶은 점수
    private int overallScore;           // 종합 평점 - TODO: 평점 평균로직 추가
    private Timestamp reviewDate;
    private String responseData;        // 평가 내용
}
