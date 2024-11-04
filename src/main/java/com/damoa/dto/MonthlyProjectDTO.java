package com.damoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProjectDTO {
    private String month;  // 월 정보 (YYYY-MM 형식)
    private int count;     // 해당 월의 프로젝트 등록 수
}
