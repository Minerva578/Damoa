package com.damoa.dto.freelancer;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FreelancerBasicInfoDTO {
    private int id;
    private int userId; // 프리랜서 ID (PK)
    private String jobPart; // 직무 (ex. 프론트엔드, 백엔드)
    private String workingStyle; // 근무 방식 (ex. 재택, 출퇴근)
    private int expectedSalary; // 희망 연봉
    private String salaryType; // 급여 방식 (ex. 시급, 월급)
    private MultipartFile mFile;
    private String originFileName; // 포트폴리오 원본 파일 이름
    private String uploadFileName; // 저장된 포트폴리오 파일 이름
    private String detail; // 상세 소개
    private String link; // 링크 (ex. github, tistory, notion)

}
