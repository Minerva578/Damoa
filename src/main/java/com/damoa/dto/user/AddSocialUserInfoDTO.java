package com.damoa.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddSocialUserInfoDTO {
    
    private String phoneNumber;
    private String email;
    private String username;
    private String socialType;
    private String userType;

}
