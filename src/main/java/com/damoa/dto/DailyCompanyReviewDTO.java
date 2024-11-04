package com.damoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyCompanyReviewDTO {
    private String date;  // 일자 정보 (YYYY-MM-DD 형식)
    private int count;    // 해당 일의 기업 리뷰 등록 개수
}
