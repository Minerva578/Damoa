package com.damoa.repository.interfaces;

import com.damoa.dto.admin.FreelancerReviewDTO;
import com.damoa.dto.admin.FreelancerReviewDetailDTO;
import com.damoa.dto.DailyFreelancerReviewDTO;
import com.damoa.dto.admin.FreelancerReviewDetailDTO;
import com.damoa.dto.review.FreelancerMainDTO;
import com.damoa.repository.model.FreelancerReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/*
* 프리랜서 리뷰 기능
* */
@Mapper
public interface FreelancerReviewRepository {

    // 프리랜서 리뷰 데이터 호출
    List<FreelancerMainDTO> findAllByFreelancerReviews();

    // 프리랜서 리뷰 id pk 호출
    Optional<FreelancerMainDTO> findByFreelancerReviewId(int id);

    // GCP 프리랜서 리뷰 데이터 실행 후 MySQL로 데이터 insert
    void insertFreelancerReview(FreelancerReview freelancerReview);

    List<FreelancerReviewDTO> findFreelancerReview(@Param("limit") int limit, @Param("offset") int offset);

    int countFreelancerReview();
    
    // 일별 프리랜서 리뷰 등록 수 데이터
    List<DailyFreelancerReviewDTO> getDailyFreelancerReviewData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    FreelancerReviewDetailDTO freelancerReviewDetail(int id);

    int delete(int id);


}
