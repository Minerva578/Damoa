package com.damoa.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Freelancer {

    private Integer id; // pk
    private Integer userId; // 프리랜서 id
    private String username; // 프리랜서 이름
    private String email; // 이메일
    private String phoneNumber; // 전화번호
    private String jobPart; // 직무
    private String workingStyle; // 근무 방식
    private Integer expectedSalary; // 희망 연봉
    private String salaryType; // 급여 방식
    private String experience; // 프리랜서 경험
    private String career; // 경력
    private String skills; // 기술 스택
    private MultipartFile mFile;
    private String originFileName; // 포트폴리오 원본
    private String uploadFileName; // 포트폴리오 저장본
    private String detail; // 상세 소개
    private String link; // 링크
    private Timestamp createdAt;
    private String keyword;

}
