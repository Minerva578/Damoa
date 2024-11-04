package com.damoa.dto.freelancer;

import java.util.List;

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
public class UpdateFreelancerCareerDTO {
    private Integer userId; // 프리랜서 ID
    private String experience; // 프리랜서 경험 (예, 아니요)
    private List<String> career; // 경력 (직무관련: ex. 풀스택 5년)
    private List<Integer> careerYear; // 경력 년차
}
