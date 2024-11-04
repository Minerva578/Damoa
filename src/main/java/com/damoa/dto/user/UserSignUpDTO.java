package com.damoa.dto.user;

import java.sql.Timestamp;

import com.damoa.repository.model.User;

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
public class UserSignUpDTO {
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String socialType; // 소셜로그인, 로컬로 나누는 변수
    private String userType; // 프리랜서인지 기업인지 나누는 변수

    // User Ojbect 반환
    public User toUser() {
        return User.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .phoneNumber(this.phoneNumber)
                .socialType(this.socialType)
                .userType(this.userType)
                .build();
    }
}
