package com.damoa.controller;

import com.damoa.constants.UserType;
import com.damoa.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor    // DI & final
public class ReviewController {

    private final ReviewService reviewService;

    // Review 메인 요청 페이지
    // 주소설계: http://localhost:8080/reviews/home
    // @return review/home 데이터 요청 메서드
    @GetMapping("/home")
    public String reviewHome(Model model) {

        reviewService.getAllReviews(model);

        return "review/home";
    }

    /*
     * 코드 중복 수정
     * 프리랜서, 컴퍼니 리뷰 리스트 페이지 요청
     * URL 쿼리 파라미터로 {type}을 넣어 두 가지 리스트 주소를 처리
     * 주소설계 : http://localhost:8080/reviews/list/{type}
     * */
    @GetMapping("/list/{type}") // URL의 {type} 부분을 변수로 처리
    public String reviewList(@PathVariable(name ="type") String type, Model model) {

        // 타입에 따라 리뷰 목록을 구분하여 추가
        if (UserType.FREELANCER.name().equalsIgnoreCase(type)) {    // 대소문자 구분 하지 않음
            reviewService.getFreelancerReviews(model);
        } else if (UserType.COMPANY.name().equalsIgnoreCase(type)) {
            reviewService.getCompanyReviews(model);
        } else {
            throw new NullPointerException("없는 페이지 입니다.");  // 파라미터 없을 시 예외 처리
        }

        return "review/" + type + "_list"; // 동적으로 뷰 이름 반환
    }

    /*
     * 프리랜서, 컴퍼니 리뷰 상세 페이지 요청
     * 주소설계 : http://localhost:8080/reviews/{type}/detail/{id}
     * @pram type
     * @pram id
     * */
    @GetMapping("/{type}/detail/{id}")
    public String reviewDetail(@PathVariable(name ="type") String type,
                               @PathVariable(name = "id") int id, Model model) {
        if (UserType.FREELANCER.name().equalsIgnoreCase(type)) {
            reviewService.getByFreelancerId(id, model);
        } else if (UserType.COMPANY.name().equalsIgnoreCase(type)) {
            reviewService.getByCompanyId(id, model);
        } else {
            throw new NullPointerException("없는 페이지 입니다.");  // 잘못된 타입 시 예외 처리
        }
        return "review/" + type + "_list" + "_detail"; // 동적으로 뷰 이름 반환
    }
}