package com.damoa.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonNaming(value =  PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenDTO {
    public String tokenType;
	public String accessToken;
	public Integer expiresIn;
	public String refreshToken;
	public Integer refreshTokenExpiresIn;
}
