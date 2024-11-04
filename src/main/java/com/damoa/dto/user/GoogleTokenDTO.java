package com.damoa.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonNaming(value =  PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleTokenDTO {

    private String accessToken;
	private String tokenType;
	private String refreshToken;
	private Integer expiresIn;
	private String scope;
	private Integer refreshTokenExpiresIn;
    
}
