package com.damoa.repository.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Blob;
import java.sql.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    private int id;
    private int type;
    private String title;
    private String content;
    private Date createdAt;
}
