package com.damoa.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyReview {
    // 프로젝트 완료 후 구글 폼으로 이동 될 때 앞 페이지에서 프리랜서와 company 아이디를 쿼리 파라미터로 가져 와야 함.
    private Integer id;
    private int writerId;
    private int freelancerId;

    // 모든 평점은 별점 1~5
    private int workQualityScore;       // 작업물 퀄리티 평가
    private int timelinessScore;        // 개발 시간 준수 정도
    private int feedbackScore;          // 개발의견 반영 정도
    private int willingnessScore;       // 함께하고 싶은 정도
    private int overallScore;           // 종합 평점
    private Timestamp reviewDate;
    private String responseData;        // 평가 내용
}
