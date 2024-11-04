package com.damoa.repository.interfaces;

import com.damoa.dto.admin.CompanyReviewDTO;
import com.damoa.dto.admin.CompanyReviewDetailDTO;
import com.damoa.dto.review.CompanyMainDTO;
import com.damoa.repository.model.CompanyReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import com.damoa.dto.DailyCompanyReviewDTO;
import com.damoa.repository.model.CompanyReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/*
 * company 리뷰 기능
 * */
@Mapper
public interface CompanyReviewRepository {

    // company 리뷰 데이터 호출
    List<CompanyMainDTO> findAllByCompanyReviews();

    // company 리뷰 id pk 호출
    Optional<CompanyMainDTO> findByCompanyReviewId(int id);

    // GCP company 리뷰 데이터 호출 후 MySQL로 데이터 insert
    void insertCompanyReview(CompanyReview companyReview);

    List<CompanyReviewDTO> findCompanyReview(@Param("limit") int limit, @Param("offset") int offset);

    int countCompanyReview();

    int delete(int id);

    CompanyReviewDetailDTO companyReviewDetail(int id);

    // 일별 기업 리뷰 등록 수 데이터 가져오기
    List<DailyCompanyReviewDTO> getDailyCompanyReviewData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 기업 리뷰 카운트
    int countComReview();
}
