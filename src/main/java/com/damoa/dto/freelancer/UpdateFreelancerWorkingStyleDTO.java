package com.damoa.dto.freelancer;

import java.sql.Timestamp;

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
public class UpdateFreelancerWorkingStyleDTO {

    private Integer id; // pk
    private Integer userId; // 프리랜서 id
    private String jobPart; // 직무
    private String workingStyle; // 근무 방식
    private Integer expectedSalary; // 희망 연봉
    private String salaryType; // 급여 방식

}
