# <다모아 : 1인 개발자-기업 간 양방향 매칭 플랫폼>
### Springboot와 Mustache를 사용한 웹사이트 제작
&nbsp; 
&nbsp;
![제목을-입력해주세요_-001 (1)](https://github.com/user-attachments/assets/49932d05-6613-4d87-b322-278c1029f0f7)
&nbsp;
### 목차
1. 프로젝트 개요
2. 구성원 및 맡은 역할
3. 서비스 환경
4. 사용 라이브러리 및 외부 API
5. 사이트 맵 (유저, 관리자)
6. 주요 기능
7. ERD 다이어그램
&nbsp; &nbsp;&nbsp;
## 1️⃣ 프로젝트 개요
### (1) 프로젝트 주제 및 목적
# 1인 개발자와 기업 간의 양방향 매칭 플랫폼
&nbsp; 
## 2️⃣ 구성원 및 맡은 역할
|이름|역할|맡은 역할|
|------|---|---|
|조현재|팀장| 프로젝트 총괄 및 팀장, 프리랜서 등록 및 검색 기능, 회원가입, 로그인 기능 구현  |
|김가령|팀원| 회원 탈퇴, 마이페이지 관리, 관리자 대시보드 구현 |
|변영준|팀원| TossAPI를 사용한 결제/환불 기능, 네이버 클로바 기반 챗봇, 공지사항 관리 기능 구현 |
|엄송현|팀원| 프로젝트 등록 및 검색 기능, 전자 서명 및 계약서 등록, 메인 페이지 UI 구현 |
|정해주|팀원| FaQ 등록 및 관리 기능, 유저 관리 기능, 리뷰 관리 기능, 광고 관리 기능 구현 |
|전명세|팀원| GoogleSheets API & GCP 기반 설문지 DB 저장 기능, 1대1 채팅 기능, 리뷰 페이지 구현|

## Notion
https://north-marscapone-03a.notion.site/0767e38bb0be4688ab6edb9022d6de11?pvs=4
&nbsp;

## PPT
[다모아ppt.pdf](https://github.com/user-attachments/files/17627227/ppt.pdf)
&nbsp;

## 보고서
[다모아 프로젝트 보고서.pdf](https://github.com/user-attachments/files/17627806/default.pdf)

## 3️⃣ Skills
| 구분                          |    내용                                                                                          | 비고|
|-------------------------------|----------------------------------------------------------------------------------------------------|-------------|
| Languages   | [![My Skills](https://skillicons.dev/icons?i=java,js,html,css&theme=light)](https://skillicons.dev)| MyBatis & Mustache |
| FrameWork   | [![My Skills](https://skillicons.dev/icons?i=spring&theme=light)](https://skillicons.dev)|
| DB   | [![My Skills](https://skillicons.dev/icons?i=mysql,mongodb&theme=light)](https://skillicons.dev)|h2|
|Cloud|[![My Skills](https://skillicons.dev/icons?i=gcp&theme=light)](https://skillicons.dev)|
|IDE|[![My Skills](https://skillicons.dev/icons?i=idea,vscode&theme=light)](https://skillicons.dev)|
|Collaboration|[![My Skills](https://skillicons.dev/icons?i=git,github&theme=light)](https://skillicons.dev)|GitHub DeskTop|
|Communication|[![My Skills](https://skillicons.dev/icons?i=discord,notion&theme=light)](https://skillicons.dev)|

## 4️⃣ 사용 라이브러리 및 외부 API
### (1) 사용 라이브러리
|라이브러리 명|버전|용도|
|------|---|---|
|jackson|2.15.0-rc1|ObjectMapper API를 통한 JSON 객체 활용|
|Lombok|1.18.34|어노테이션을 활용한 간단한 메서드 사용 및 편의성 증가|
|MySQL Connecter J|8.0.21|Java와 MySQL을 연결한 효율적인 데이터베이스 사용|
|Chart.js|4.4.4|차트를 사용한 효과적인 데이터 시각화 및 대시보드 제작|
|Spring Security Crypto|---|SpringBoot 기반의 간편한 인증 및 보안 처리|
|Spring-Boot-Starter-Websocket|3.0.3|전 이중 통신 및 양방향 통신을 위한 연결 지향 통신 제공|
|DevTools|3.2.10|개발 편의성(라이브 리로드)|
|STOMP|2.3.3|WebSocket 메시지 프로토콜|
|SocketJS|1.1.2|WebSocket 호환성 지원|
|JSON|2021.03.07|JSON 데이터 처리|
|jQuery|3.6.4|AJAX 호출 및 프론트엔드 편의성|

### (2) 사용 외부 API
|기능|API 명|제공|용도|
|------|---|---|---|
|로그인|카카오 로그인|Kakao Devolpers|카카오 소셜 로그인을 통한 간편 로그인 기능|
|로그인|구글 로그인|Google Cloud|구글 소셜 로그인을 통한 간편 로그인 기능|
|휴대폰 문자 인증|CoolSms|CoolSms|문자 인증을 통한 보안 및 인증 처리|
|결제 및 환불|Toss 결제 / 결제 취소 API|Toss Payments|사용자 결제 승인, 환불, 취소|
|리뷰|구글 Sheets API|Google WorkSpace|구글 폼 리뷰 설문지 작성 및 스프레드 시트 연동|
|챗봇|네이버 클로바 AI|Clova Developers Console|네이버 클로바 기반 AI 챗봇|

## 5️⃣ 사이트맵
![007](https://github.com/user-attachments/assets/a0634534-2fce-439d-a58e-4ab2e2bc7ee3)

## 6️⃣ ERD 다이어그램
![006](https://github.com/user-attachments/assets/c360ff1e-4ca2-4cd6-92c9-bb544be30bf2)

## 7️⃣ 주요 기능 및 화면 소개 &nbsp;
### 1. 사용자 (기업/프리랜서)
#### (1) 로그인 및 회원가입 (구글, 카카오 소셜 로그인)
![image](https://github.com/user-attachments/assets/a0da2f5a-a279-47f6-a311-19d5eb5ec79b)
![image](https://github.com/user-attachments/assets/aebc7f70-0464-4553-822d-189e83dc4bd9)
![image](https://github.com/user-attachments/assets/54c9472b-8183-4a83-9512-05713bceb963)
![010](https://github.com/user-attachments/assets/a057d7f8-4471-46e7-8263-9586c8cdb602)
![009](https://github.com/user-attachments/assets/e0a55a45-46c4-4d65-bff3-9b687b491564)

#### (2) 메인 화면
![008](https://github.com/user-attachments/assets/b698102d-1c71-4332-bac7-70ad695d06d7)

#### (3) 프로젝트 등록
![019](https://github.com/user-attachments/assets/c8633fb9-4de8-4225-90a2-bb8f7f7d2096)

#### (4 - 1) 프리랜서 기본 정보 관리
![image](https://github.com/user-attachments/assets/d827dc69-f8a7-41ef-8ed7-69ca441bb8cf)
![image](https://github.com/user-attachments/assets/f6ee7809-a9b7-462c-a2eb-281d11ec5432)
![014](https://github.com/user-attachments/assets/cc78148a-3ee9-4fc7-9506-e64174e7fcc8)

#### (4 - 2) 프리랜서 스킬 스택 관리
![017](https://github.com/user-attachments/assets/57c782c4-16c8-4651-8770-70daddf7cb24)

#### (4 - 3) 프리랜서 경력 관리
![018](https://github.com/user-attachments/assets/ae72a964-c08f-467f-8953-fa8324b28561)

#### (4 - 4) 프리랜서 신청 프로젝트
![image](https://github.com/user-attachments/assets/9808a7d4-1a32-4e9c-b82f-93d5d813d0c8)

#### (4 - 5) 프리랜서 참여, 마감 프로젝트
![image](https://github.com/user-attachments/assets/275b4571-0a28-4361-b514-73388faac88e)

#### (5) 프로젝트 찾기 (목록)
![015](https://github.com/user-attachments/assets/100785e0-bd6e-4634-9dd0-d16e3462738b)

#### (6) 프로젝트 찾기 (디테일)
![img1 daumcdn](https://github.com/user-attachments/assets/09fe2b11-6879-4db2-b25e-b4ebb3cb5a19)

#### (7) 프리랜서 찾기 (목록)
![image](https://github.com/user-attachments/assets/3ef2ac17-ae8d-4151-b469-5a6946ad4a78)

#### (8) 프리랜서 찾기 (디테일)
![image](https://github.com/user-attachments/assets/f42610bb-60c2-4b7f-bc60-d2146f96a3f6)

#### (9) 1:1 채팅 기능
![image](https://github.com/user-attachments/assets/df6ac27c-a3f7-4527-9559-e59246eb7923)
![image](https://github.com/user-attachments/assets/d6daad04-f52e-4429-ba98-f1b631585b3e)

#### (10 - 1) 포인트 충전
![020](https://github.com/user-attachments/assets/71c6bc13-cb4f-4bfb-abf6-b0c2515fd1dd)

#### (10 - 2) 포인트 환불
![021](https://github.com/user-attachments/assets/623aedd6-1e58-4702-bb8d-cf42a129f9bd)

#### (11) 리뷰 게시판 (홈)
![image](https://github.com/user-attachments/assets/8cb716d4-f0fe-46ac-861b-0b1983fb3466)
#### (12) 리뷰 게시판 (목록)
![image](https://github.com/user-attachments/assets/5ce1c3bb-44aa-400d-9f04-bc3030ea4f2e)
#### (13) 리뷰 게시판 (디테일)
![image](https://github.com/user-attachments/assets/389311ce-a717-4c77-8a06-cd8d3ffecfca)
#### (14) FaQ 게시판
![012](https://github.com/user-attachments/assets/937164ae-0016-4fe2-9ab6-efa29670bf2b)

#### (15) 공지사항 게시판
![011](https://github.com/user-attachments/assets/b3edbd72-1bac-44c7-97b5-ed28000ec906)

#### (16) 기업 내 프로젝트 관리
![023](https://github.com/user-attachments/assets/ba5b753b-51b9-4ecf-8f51-01ec32191b28)

#### (17) 1:1 채팅봇 : 모아봇
![025](https://github.com/user-attachments/assets/c0d8c04c-9944-411e-a573-be05457a014c)


### 2. 관리자 
#### (1) 로그인
![image](https://github.com/user-attachments/assets/c680075c-b12c-4266-99b5-f870634256ac)

#### (2) 대쉬보드 (관리자 메인 페이지)
![028](https://github.com/user-attachments/assets/bbee4f5e-05e8-43dc-9fe5-371dcb604c80)

#### (3) 회원 관리
![029](https://github.com/user-attachments/assets/ea4efb02-4b1e-4403-9232-e3cb87d48ddc)

#### (4) 결제 관리
![image](https://github.com/user-attachments/assets/41f00e1a-8d57-4df2-8e94-51ec8324039c)

#### 환불 관리
![022](https://github.com/user-attachments/assets/55aeeaba-be4b-4824-a76e-289ef789622d)

#### 광고 관리
![030](https://github.com/user-attachments/assets/9ac03850-bd74-445c-a427-059705734a80)

#### 고객 지원
![032](https://github.com/user-attachments/assets/a7618287-000d-4528-8f4b-3ed2fdced3ab)

#### 리뷰 관리
![033](https://github.com/user-attachments/assets/870600f0-f048-40ec-b8d7-d0a3d1777856)

