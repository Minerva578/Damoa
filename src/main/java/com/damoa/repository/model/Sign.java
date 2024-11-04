package com.damoa.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Sign {
    private int id;
    private int userId;
    private String name;
    private String fileName;
    private int status;
    private Timestamp createdAt;

    public String setUpSignImage(){
        return fileName==null?
                "":"/images/signs/"+fileName;
    }
}
