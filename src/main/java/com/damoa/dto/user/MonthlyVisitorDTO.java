package com.damoa.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyVisitorDTO {
    private String month;  // 월 정보 (YYYY-MM 형식)
    private int count;     // 해당 월의 방문자 수
}
