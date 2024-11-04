package com.damoa.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddSignDTO {
    private String name;
    private byte[] fileData;
    private int userId;
    private String uploadFileName;
}
