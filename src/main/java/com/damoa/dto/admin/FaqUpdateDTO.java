package com.damoa.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqUpdateDTO {

    private int id;
    private String title;
    private String content;

}
