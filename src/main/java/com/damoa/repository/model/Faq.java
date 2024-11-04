package com.damoa.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Faq {

    private int id;
    private int type;
    private String title;
    private String content;
    private Date createdAt;


}
