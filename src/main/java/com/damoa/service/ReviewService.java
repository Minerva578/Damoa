package com.damoa.service;

import com.damoa.dto.admin.CompanyReviewDTO;
import com.damoa.dto.admin.CompanyReviewDetailDTO;
import com.damoa.dto.admin.FreelancerReviewDTO;
import com.damoa.dto.DailyCompanyReviewDTO;
import com.damoa.dto.DailyFreelancerReviewDTO;
import com.damoa.dto.admin.FreelancerReviewDetailDTO;
import com.damoa.dto.review.CompanyMainDTO;
import com.damoa.dto.review.FreelancerMainDTO;
import com.damoa.dto.admin.NoticeDTO;
import com.damoa.repository.interfaces.CompanyReviewRepository;
import com.damoa.repository.interfaces.FreelancerReviewRepository;
import com.damoa.repository.interfaces.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor    // DI : 의존성 주입 & final
public class ReviewService {

    private final UserRepository userRepository;
    private final CompanyReviewRepository companyReviewRepo;
    private final FreelancerReviewRepository freelancerReviewRepo;

    // 클라이언트 && 프리랜서 리뷰 데이터 렌더링
    public void getAllReviews(Model model) {
        // 1. 클라이언트와 프리랜서 데이터 가져옴
        List<CompanyMainDTO> companyReviews = companyReviewRepo.findAllByCompanyReviews();
        List<FreelancerMainDTO> freelancerReviews = freelancerReviewRepo.findAllByFreelancerReviews();

        model.addAttribute("companyReviews", companyReviews);
        model.addAttribute("freelancerReviews", freelancerReviews);
        log.info("companyReviews: {}, freelancerReviews: {}", companyReviews, freelancerReviews);
    }

    // 프리랜서 리뷰 데이터 호출 메서드
    public void getFreelancerReviews(Model model) {
        List<FreelancerMainDTO> freelancerReview = freelancerReviewRepo.findAllByFreelancerReviews();
        model.addAttribute("freelancerReview", freelancerReview);
        model.addAttribute("totalReviews", freelancerReview.size());
    }

    // 회사 리뷰 데이터 호출 메서드
    public void getCompanyReviews(Model model) {
        List<CompanyMainDTO> companyReview = companyReviewRepo.findAllByCompanyReviews();
        model.addAttribute("companyReview", companyReview);
        model.addAttribute("totalReviews", companyReview.size());
    }

    // 프리랜서 리뷰 상세 조회 기능
    public void getByFreelancerId(int id, Model model) {
        Optional<FreelancerMainDTO> freelancerReview = freelancerReviewRepo.findByFreelancerReviewId(id);

        model.addAttribute("freelancerReview", freelancerReview.get()); // Optional에서 값 추출
    }

    // 회사 리뷰 상세 조회 기능
    public void getByCompanyId(int id, Model model) {
        Optional<CompanyMainDTO> companyReview = companyReviewRepo.findByCompanyReviewId(id);

        model.addAttribute("companyReview", companyReview.get()); // Optional에서 값 추출 .get() 안쓰면 내부 객체 접근 불가
        log.info("companyReview: {}", companyReview);
    }

    public Page<CompanyReviewDTO> getComapanyReviews(Pageable pageable) {

        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<CompanyReviewDTO> companyList = companyReviewRepo.findCompanyReview(pageable.getPageSize(), offset);
        int totalCount = companyReviewRepo.countComReview();
        return new PageImpl<>(companyList, pageable, totalCount);
    }

    public int countReview() {
        return companyReviewRepo.countCompanyReview();
    }

    public Page<FreelancerReviewDTO> findFreelancerReview(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<FreelancerReviewDTO> freelancerReviews = freelancerReviewRepo.findFreelancerReview(pageable.getPageSize(), offset);
        int totalCount = freelancerReviewRepo.countFreelancerReview();
        return new PageImpl<>(freelancerReviews, pageable, totalCount);
    }

    public int countFreelancerReview() {
        return freelancerReviewRepo.countFreelancerReview();
    }



    // 일별 기업 리뷰 등록 수 데이터
    public List<DailyCompanyReviewDTO> getDailyCompanyReviewData() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return companyReviewRepo.getDailyCompanyReviewData(startDate.format(formatter), endDate.format(formatter));
    }

    // 일별 프리랜서 리뷰 등록 수 데이터
    public List<DailyFreelancerReviewDTO> getDailyFreelancerReviewData() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return freelancerReviewRepo.getDailyFreelancerReviewData(startDate.format(formatter), endDate.format(formatter));
    }

    public int deleteCompanyReview(int id){
        return companyReviewRepo.delete(id);
    }

    public int deleteFreelancerReview(int id){
        return freelancerReviewRepo.delete(id);
    }

    public FreelancerReviewDetailDTO getFreelancerDetails(int id){
        return freelancerReviewRepo.freelancerReviewDetail(id);
    }

}
