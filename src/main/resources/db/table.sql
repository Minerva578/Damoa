-- User 테이블
create table user(
	id int auto_increment primary key,
    email varchar(255) not null unique,
    username varchar(255) not null,
    password varchar(255) not null,
    phone_number varchar(11) not null unique,
    social_type enum ('local', 'kakao', 'google', 'naver') default 'local' not null,
    user_type enum('freelancer', 'company') not null,
    created_at timestamp default current_timestamp
);

CREATE TABLE `company_review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int DEFAULT NULL, -- 클라이언트 ID
  `freelancer_id` int DEFAULT NULL, -- 프리랜서 ID
  `work_quality_score` int NOT NULL, -- 작업물 퀄리티 평가 (1~5)
  `timeliness_score` int NOT NULL, -- 개발 시간 준수 정도 (1~5)
  `feedback_score` int NOT NULL, -- 개발 의견 반영 정도 (1~5)
  `willingness_score` int NOT NULL, -- 함께 하고 싶은 정도 (1~5)
  `overall_score` int NOT NULL, -- 종합 평점 (1~5)
  `review_date` timestamp DEFAULT CURRENT_TIMESTAMP, -- 리뷰 작성 날짜
  `response_data` TEXT DEFAULT NULL, -- 평가 내용 (TEXT 형식)
  PRIMARY KEY (`id`),
  CONSTRAINT `clients_review_chk_1` CHECK (`work_quality_score` BETWEEN 1 AND 5),
  CONSTRAINT `clients_review_chk_2` CHECK (`timeliness_score` BETWEEN 1 AND 5),
  CONSTRAINT `clients_review_chk_3` CHECK (`feedback_score` BETWEEN 1 AND 5),
  CONSTRAINT `clients_review_chk_4` CHECK (`willingness_score` BETWEEN 1 AND 5),
  CONSTRAINT `clients_review_chk_5` CHECK (`overall_score` BETWEEN 1 AND 5)
);

CREATE TABLE `freelancers_review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_id` int, -- 클라이언트 ID
  `freelancer_id` int, -- 프리랜서 ID
  `communication_score` int NOT NULL, -- 커뮤니케이션 점수 (1~5)
  `timeliness_score` int NOT NULL, -- 개발 시간 준수 정도 (1~5)
  `support_score` int NOT NULL, -- 지원 점수 (1~5)
  `willingness_score` int NOT NULL, -- 함께 하고 싶은 정도 (1~5)
  `overall_score` int NOT NULL, -- 종합 평점 (1~5)
  `review_date` timestamp DEFAULT CURRENT_TIMESTAMP, -- 리뷰 작성 날짜
  `response_data` TEXT DEFAULT NULL, -- 평가 내용 (TEXT 형식)
  PRIMARY KEY (`id`),
  CONSTRAINT `freelancers_review_chk_1` CHECK (`communication_score` BETWEEN 1 AND 5),
  CONSTRAINT `freelancers_review_chk_2` CHECK (`timeliness_score` BETWEEN 1 AND 5),
  CONSTRAINT `freelancers_review_chk_3` CHECK (`support_score` BETWEEN 1 AND 5),
  CONSTRAINT `freelancers_review_chk_4` CHECK (`willingness_score` BETWEEN 1 AND 5),
  CONSTRAINT `freelancers_review_chk_5` CHECK (`overall_score` BETWEEN 1 AND 5)
);
