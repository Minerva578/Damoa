package com.damoa.repository.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BusinessType {
    private int id;
    private String type; // 사업자 유형
}
