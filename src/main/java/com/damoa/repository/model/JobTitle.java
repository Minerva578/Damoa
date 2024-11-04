package com.damoa.repository.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JobTitle {
    private int id;
    private String title; // 직무 유형
}
