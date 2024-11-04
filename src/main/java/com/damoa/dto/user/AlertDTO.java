package com.damoa.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDTO {

    private String username;
    private String userType;
    private int amount;
    private Timestamp createdAt;
    private String checked;
}
