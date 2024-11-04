package com.damoa.service;

import com.damoa.constants.UserType;
import com.damoa.dto.review.ReviewUserDTO;
import com.damoa.repository.interfaces.CompanyReviewRepository;
import com.damoa.repository.interfaces.FreelancerReviewRepository;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.CompanyReview;
import com.damoa.repository.model.FreelancerReview;
import com.damoa.repository.model.User;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    private final UserRepository userRepository;
    private final CompanyReviewRepository companyReviewRepo;
    private final FreelancerReviewRepository freelancerReviewRepo;
    private static final String APPLICATION_NAME = "Google_Sheets_API_Reviews";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();


    // Google Sheets API 인증 설정
    public Sheets getCloudAccountKey() throws Exception {

        // 파일 주소는 상대경로 사용(팀원들의 절대경로가 모두 다르기 때문)
        InputStream filePath = new FileInputStream("../Damoa/src/main/resources/google_sheet_api/google_sheets.json");
        Credential credential = GoogleCredential.fromStream(filePath)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));  // SCOPES 사용

        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /*
     * @param spreadsheetId: Google Sheets 문서의 ID
     * @param range: 범위 (예: "A2:I11")
     * @param reviewType: REVIEW_TYPE
     */
    public String importReviews(String spreadsheetId, String range, UserType reviewType) throws Exception {
        Sheets sheetsKey = getCloudAccountKey();
        List<List<Object>> values = sheetsKey.spreadsheets().values().get(spreadsheetId, range).execute().getValues();

        if (values != null && !values.isEmpty()) {
            for (List<Object> row : values) {
                saveReview(row, reviewType);
            }
            deleteRowsFromSheet(sheetsKey, spreadsheetId, values.size(), reviewType);
        } else {
            return reviewType + " 리뷰 데이터가 없습니다.";
        }
        return "데이터가 성공적으로 삽입되었습니다.";
    }

    // REVIEW TYPE에 따라 각 테이블에 데이터 저장
    public void saveReview(List<Object> row, UserType reviewType) {
        // 유효성 검사
        // 1. row 6과 row 8에 있는 userName 값 확인
        String writerName = row.get(6) != null ? row.get(6).toString().trim() : "";
        String collaboraiterName = row.get(7) != null ? row.get(7).toString().trim() : "";

        // 2. 빈 문자열인 경우 종료
        if (writerName.isEmpty() || collaboraiterName.isEmpty()) {
            return;
        }

        // 3. 데이터베이스에서 조회
        Optional<User> writerUser = userRepository.findByUserName(writerName);
        Optional<User> collaboraiterUser = userRepository.findByUserName(collaboraiterName);

        // 4. DB에 존재하면 ReviewUserDTO로 변환
        ReviewUserDTO writerUserDTO = writerUser.map(user -> new ReviewUserDTO(user.getId(), user.getUsername())).orElse(null);
        ReviewUserDTO collaboraiterDTO = collaboraiterUser.map(user -> new ReviewUserDTO(user.getId(), user.getUsername())).orElse(null);

        // 5. companyDTO 또는 freelancerDTO null이면 종료
        if (writerUserDTO == null || collaboraiterDTO == null) {
            return;
        }

        int writerId = writerUserDTO.getId();
        int collaboraiterId = collaboraiterDTO.getId();
        try {
            int score1 = parseScore(row.get(1));
            int score2 = parseScore(row.get(2));
            int score3 = parseScore(row.get(3));
            int score4 = parseScore(row.get(4));
            int overallScore = (score1 + score2 + score3 + score4) / 4;
            Timestamp reviewDate = new Timestamp(System.currentTimeMillis());

            // REVIEW_TYPE에 따라 각각 저장
            if (reviewType.name().equalsIgnoreCase(UserType.COMPANY.name())) {
                CompanyReview companyReview = new CompanyReview(
                        null, writerId, collaboraiterId, score1, score2, score3, score4, overallScore, reviewDate,
                        row.get(5) != null ? row.get(5).toString() : null
                );
                companyReviewRepo.insertCompanyReview(companyReview);
            } else if (reviewType.name().equalsIgnoreCase(UserType.FREELANCER.name())) {
                FreelancerReview freelancerReview = new FreelancerReview(
                        null, writerId, collaboraiterId, score1, score2, score3, score4, overallScore, reviewDate,
                        row.get(5) != null ? row.get(5).toString() : null
                );
                freelancerReviewRepo.insertFreelancerReview(freelancerReview);
            } else {
                throw new IllegalArgumentException("올바른 리뷰 유형이 아닙니다 : " + reviewType);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("리뷰 점수는 숫자 형식이어야 합니다: " + row.get(1));
        }
    }

    // Pair 1-1. 리뷰 별점 점수를 string -> 숫자형식로 변환하는 메서드
    private int parseScore(Object score) {
        if (score == null || !(score instanceof String)) {
            throw new NumberFormatException("점수는 null이 아닌 문자열이어야 합니다.");
        }

        String scoreStr = (String) score;

        // isNumeric() 메서드 호출
        if (!isNumeric(scoreStr)) {
            throw new NumberFormatException("점수는 숫자 형식이어야 합니다: " + scoreStr);
        }

        return Integer.parseInt(scoreStr);
    }

    // Pair 1-2. 문자열이 정수인지 확인하는 메서드
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
    * 구글 시트 row 삭제 기능
    * @param sheetsService, spreadsheetId, values.size(), reviewType
    * */
    public void deleteRowsFromSheet(Sheets sheetsService, String spreadsheetId, int rowCount, UserType reviewType) throws Exception {
        // 구글 시트 gid=ID
        // TODO: 시트 ID와 스프레드시트 ID secure.yml에 저장 필요
        int sheetId = reviewType.name().equalsIgnoreCase(UserType.COMPANY.name()) ? 958507219 : 91723201; // 컴퍼니 리뷰인 경우 컴퍼니 ID, 아닐 경우 프리랜서 ID
        int startRow = 1; //삭제할 시작 행 (0부터 시작하므로 A2는 인덱스 1)

        // 행 삭제를 위한 요청 설정
        BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(
                        new Request().setDeleteDimension(
                                new DeleteDimensionRequest()
                                        .setRange(new DimensionRange()
                                                .setSheetId(sheetId)
                                                .setDimension("ROWS") // 행 삭제
                                                .setStartIndex(startRow)
                                                .setEndIndex(startRow + rowCount) // 삭제할 끝 행 (rowCount 만큼)
                                        )
                        )
                ));

        // 요청 실행
        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchRequest).execute();
        // 삭제 성공 로그
        log.info("Google Sheets의 행이 성공적으로 삭제되었습니다.");
    }

}
