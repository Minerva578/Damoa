package com.damoa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyFreelancerReviewDTO {
    private String date; // 일자 정보
    private int count;
}
