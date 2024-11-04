package com.damoa.dto.user;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoResponseDTO {
    private Long id;
	private String connectedAt;
	private Properties properties;
	private KakaoAccount kakaoAccount;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
	public class Properties {
 
		private String nickname;
		private String profileImage;
		private String thumbnailImage;
		
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
	public class KakaoAccount {
 
		private String email;

	}


	
}
