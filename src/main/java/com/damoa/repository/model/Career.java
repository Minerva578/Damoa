package com.damoa.repository.model;

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
public class Career {

    private int id;
    private String career; // 경력명 (ex. 풀스택, 프론트엔드 등)

}
