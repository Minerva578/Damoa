package com.damoa.controller;

import com.damoa.constants.UserType;
import com.damoa.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@EnableScheduling
public class GoogleSheetsController {

    private final GoogleSheetsService googleSheetsService;

    /*
     * 주소설계 : http://localhost:8080/reviews/company/import
     */
    @GetMapping("/reviews/company/import")
    public void companyReviewImport() throws Exception {
        googleSheetsService.importReviews("12NbvSzLWngg4Vt9G5ZjfPCj-g5mfhY2jrdxTxtLXdQ8", "A2:I11", UserType.COMPANY);
    }

    /*
     * 주소설계 : http://localhost:8080/reviews/freelancer/import
     */
    @GetMapping("/reviews/freelancer/import")
    public void freelancerReviewImport() throws Exception {
        googleSheetsService.importReviews("12CoTTvlEsD-hJSbVOdwaQsMNneSVvfnMHxt3r24z6Lo", "A2:I11", UserType.FREELANCER);
    }

/*    *//*
     * 1분마다 회사 리뷰 데이터 자동으로 가져오기
     *//*
    @Scheduled(fixedRate = 30000) // 30초(30,000 ms)마다 실행
    public void scheduleCompanyReviewImport() throws Exception {
        companyReviewImport();
    }

    *//*
     * 1분마다 프리랜서 리뷰 데이터 자동으로 가져오기
     *//*
    @Scheduled(fixedRate = 30000)
    public void scheduleFreelancerReviewImport() throws Exception {
        freelancerReviewImport();
    }*/
}
