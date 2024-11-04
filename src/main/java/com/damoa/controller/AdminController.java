package com.damoa.controller;

import com.damoa.dto.TossHistoryDTO;
import com.damoa.dto.admin.AdDTO;
import com.damoa.dto.admin.AdminSignInDTO;
import com.damoa.dto.admin.CompanyReviewDTO;
import com.damoa.dto.admin.FreelancerReviewDTO;
import com.damoa.dto.admin.NoticeDTO;
import com.damoa.dto.user.MonthlyRegisterDTO;
import com.damoa.dto.user.MonthlyVisitorDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.Ad;
import com.damoa.repository.model.Admin;
import com.damoa.repository.model.Notice;
import com.damoa.repository.model.User;
import com.damoa.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import com.damoa.dto.DailyCompanyReviewDTO;
import com.damoa.dto.DailyFreelancerReviewDTO;
import com.damoa.dto.MonthlyFreelancerDTO;
import com.damoa.dto.MonthlyProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Value("${file.upload-dir-ad}")
    private String uploadAddir;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final VisitorService visitorService;

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    private final ProjectService projectService;

    @Autowired
    private final PaymentService payService;

    @Autowired
    private final FreelancerService freelancerService;

    @Autowired
    private final NoticeService noticeService;

    /**
     * 관리자 메인 페이지
     *
     * @return
     */
    @GetMapping("/main")
    public String mainPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Admin admin = session != null ? (Admin) session.getAttribute("admin") : null;

        // 로그인을 하지 않았을 경우 로그인 페이지로 리다이렉트
        if (admin == null) {
            return "redirect:/admin/sign-in";
        }

        // 방문자의 IP 주소를 가져와서 방문자 기록
        String userIp = request.getRemoteAddr(); // 클라이언트의 IP 주소를 얻음
        visitorService.recordVisitor(userIp); // 방문자 기록

        return "/admin/main";
    }

    /**
     * 관리자 로그인 페이지
     *
     * @return
     */
    @GetMapping("/sign-in")
    public String adminSignInPage() {
        return "admin/sign_in";
    }

    /**
     * 관리자 로그인
     *
     * @return
     */
    @PostMapping("/sign-in")
    public String adminSignInProc(AdminSignInDTO adminSignInDTO, HttpServletRequest request) {
        try {
            Admin admin = adminService.findAdmin(adminSignInDTO);
            // 세션 생성
            HttpSession session = request.getSession(true);
            session.setAttribute("admin", admin);
            return "redirect:/admin/main";
        } catch (Exception e) {
            if (adminSignInDTO.getUsername() == null || adminSignInDTO.getUsername().isEmpty()) {
                throw new DataDeliveryException("아이디를 입력하세요.", HttpStatus.BAD_REQUEST);
            }
            if (adminSignInDTO.getPassword() == null || adminSignInDTO.getPassword().isEmpty()) {
                throw new DataDeliveryException("비밀번호를 입력하세요.", HttpStatus.BAD_REQUEST);
            }
            e.printStackTrace();
            return "/admin/sign_in";
        }
    }

    @GetMapping("/management")
    public String UserListPage(@PageableDefault(size = 10) Pageable pageable, Model model) {


        Page<User> userPage = adminService.getAllUser(pageable);
        List<User> userList = userPage.getContent();


        // 페이지 정보를 계산하여 모델에 추가
        int currentPage = userPage.getNumber(); // 현재 페이지 번호 (0부터 시작)
        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", currentPage); // 페이지를 1부터 시작하기 위해 +1
        model.addAttribute("totalPages", userPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < userPage.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", userPage);

        return "/admin/admin_user_list";
    }

    /**
     * 월별 회원 수 데이터 반환
     *
     * @return
     */
    @GetMapping("/monthly-registers")
    public ResponseEntity<List<MonthlyRegisterDTO>> getMonthlyRegisterData() {
        List<MonthlyRegisterDTO> registerDataList = adminService.getMonthlyRegisterData();
        return new ResponseEntity<>(registerDataList, HttpStatus.OK); // JSON 형식으로 반환
    }

    /**
     * 월별 방문자 수 데이터 반환
     *
     * @return JSON 데이터
     */
    @GetMapping("/monthly-visitors")
    public ResponseEntity<List<MonthlyVisitorDTO>> getMonthlyVisitorData() {
        List<MonthlyVisitorDTO> visitorDataList = visitorService.getMonthlyVisitorData();
        return new ResponseEntity<>(visitorDataList, HttpStatus.OK);

    }

    /**
     * 모든 결제 내역
     *
     * @param model
     * @return
     */
    @GetMapping("/Pay-Management")
    public String paymemtHistoryPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        // 결제 내역 조회
        Page<TossHistoryDTO> paymentPage = payService.findAll(pageable);

        // 날짜 포맷팅을 위한 DateTimeFormatter 설정
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 숫자 포맷팅을 위한 NumberFormat 설정
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        // 각 결제 내역의 requestedAt과 amount를 포맷팅하여 새로운 리스트 생성
        List<TossHistoryDTO> formattedPaymentList = paymentPage.stream()
                .map(payment -> formatPayment(payment, inputFormatter, outputFormatter, numberFormat))
                .collect(Collectors.toList());


        // 페이지 정보를 계산하여 모델에 추가
        int currentPage = paymentPage.getNumber(); // 현재 페이지 번호 (0부터 시작)
        model.addAttribute("currentPage", currentPage); // 페이지를 1부터 시작하기 위해 +1
        model.addAttribute("totalPages", paymentPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < paymentPage.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", paymentPage);
        model.addAttribute("paymentList", formattedPaymentList);

        return "/admin/admin_management_payment";
    }

    /**
     * 환불
     *
     * @param model
     * @return
     */
    @GetMapping("/refund")
    public String refundApprovalPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        // 결제 내역 조회
        Page<TossHistoryDTO> paymentPage = payService.findRequestedRefund(pageable);

        // 날짜 포맷팅을 위한 DateTimeFormatter 설정
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 숫자 포맷팅을 위한 NumberFormat 설정
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        // 각 결제 내역의 requestedAt과 amount를 포맷팅하여 새로운 리스트 생성
        List<TossHistoryDTO> formattedPaymentList = paymentPage.stream()
                .map(payment -> formatPayment(payment, inputFormatter, outputFormatter, numberFormat)) // 메서드 호출
                .collect(Collectors.toList());

        /// 페이지 정보를 계산하여 모델에 추가
        int currentPage = paymentPage.getNumber(); // 현재 페이지 번호 (0부터 시작)
        model.addAttribute("currentPage", currentPage); // 페이지를 1부터 시작하기 위해 +1
        model.addAttribute("totalPages", paymentPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < paymentPage.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", paymentPage);
        model.addAttribute("paymentList", formattedPaymentList);

        return "/admin/admin_refund_approval";
    }

    /**
     * http://localhost:8080/admin/list/company
     *
     * @param model
     * @return
     */
    @GetMapping("/list/company") // URL의 {type} 부분을 변수로 처리
    public String companyReviewList(@PageableDefault(size = 5) Pageable pageable, Model model) {


        Page<CompanyReviewDTO> reviewPage = reviewService.getComapanyReviews(pageable);
        List<CompanyReviewDTO> reviewList = reviewPage.getContent();

        int currentPage = reviewPage.getNumber();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", reviewPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < reviewPage.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", reviewPage);
        model.addAttribute("reviewList", reviewList);

        return "admin/company_list";
    }

    @GetMapping("/list/freelancer")
    public String freelancerReviewList(@PageableDefault(size = 5) Pageable pageable, Model model) {

        int totallist = reviewService.countFreelancerReview(); // 총몇개의 row 인지 확인


        Page<FreelancerReviewDTO> reviewPage = reviewService.findFreelancerReview(pageable);
        List<FreelancerReviewDTO> reviewList = reviewPage.getContent();

        int currentPage = reviewPage.getNumber();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", reviewPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < reviewPage.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", reviewPage);
        model.addAttribute("reviewList", reviewList);

        return "admin/freelancer_list";
    }


    /**
     * 월별 프로젝트 등록 수 데이터 반환
     *
     * @return
     */
    @GetMapping("/monthly-projects")
    public ResponseEntity<List<MonthlyProjectDTO>> getMonthlyProjectData() {
        List<MonthlyProjectDTO> projectDataList = projectService.getMonthlyProjectData();
        return new ResponseEntity<>(projectDataList, HttpStatus.OK);
    }

    /**
     * 월별 프리랜서 등록 수 데이터 반환
     *
     * @return
     */
    @GetMapping("/monthly-freelancers")
    public ResponseEntity<List<MonthlyFreelancerDTO>> getMonthlyFreelancerData() {
        List<MonthlyFreelancerDTO> freelancerDataList = freelancerService.getMonthlyFreelancerData();
        return new ResponseEntity<>(freelancerDataList, HttpStatus.OK);
    }

    // 일별 기업 리뷰 데이터 반환 API
    @GetMapping("/daily-company-reviews")
    public ResponseEntity<List<DailyCompanyReviewDTO>> getDailyCompanyReviewData() {
        List<DailyCompanyReviewDTO> companyReviewDataList = reviewService.getDailyCompanyReviewData();
        return new ResponseEntity<>(companyReviewDataList, HttpStatus.OK);
    }

    // 일별 프리랜서 리뷰 데이터 반환 API
    @GetMapping("/daily-freelancer-reviews")
    public ResponseEntity<List<DailyFreelancerReviewDTO>> getDailyFreelancerReviewData() {
        List<DailyFreelancerReviewDTO> freelancerReviewDataList = reviewService.getDailyFreelancerReviewData();
        return new ResponseEntity<>(freelancerReviewDataList, HttpStatus.OK);
    }

    /**
     * 결제 내역의 amount를 포맷팅하는 메서드
     *
     * @param payment 결제 내역 DTO
     * @return 포맷팅된 결제 내역 DTO
     */
    // 결제 내역 포맷팅 메서드
    private TossHistoryDTO formatPayment(TossHistoryDTO payment, DateTimeFormatter inputFormatter,
                                         DateTimeFormatter outputFormatter, NumberFormat numberFormat) {
        // String 타입의 requestedAt 필드 포맷팅
        String originalDateStr = payment.getRequestedAt();
        String formattedDate = OffsetDateTime.parse(originalDateStr, inputFormatter)
                .format(outputFormatter);
        payment.setRequestedAt(formattedDate);

        // amount를 쉼표가 포함된 형식으로 포맷팅
        if (payment.getAmount() != null) { // amount가 null이 아닐 경우만 포맷팅
            try {
                double amountValue = Double.parseDouble(payment.getAmount());
                String formattedAmount = numberFormat.format(amountValue);
                payment.setAmount(formattedAmount);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // 숫자 변환 시 예외 발생 시 로그 출력
                // 예외가 발생하면 포맷팅하지 않고 그대로 유지
            }
        }

        return payment; // 포맷팅된 결제 내역 반환
    }

    @GetMapping("/notice")
    public String noticeListPage(@PageableDefault(size = 5) Pageable pageable, Model model) {

        // 모든 공지 가져오기
        Page<NoticeDTO> notice = adminService.getAllNotice(pageable); // 모든 공지 개수
        List<NoticeDTO> noticeList = notice.getContent();

        /// 페이지 정보를 계산하여 모델에 추가
        int currentPage = notice.getNumber(); // 현재 페이지 번호 (0부터 시작)
        model.addAttribute("currentPage", currentPage); // 페이지를 1부터 시작하기 위해 +1
        model.addAttribute("totalPages", notice.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < notice.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", notice);
        model.addAttribute("noticeList", noticeList);

        return "/admin/notice";
    }

    @GetMapping("/notice/detail/{id}")
    public String noticeDetailPage(@PathVariable("id") int id, Model model) {
        Notice notice = noticeService.getNotice(id);

        model.addAttribute("notice", notice);
        System.out.println(notice);
        return "/admin/notice_detail";
    }


    @GetMapping("/ad/active/ads")
    public String activeAd(Model model) {

        List<AdDTO> activeAds = adminService.activeAd();
        model.addAttribute("activeAds", activeAds);
        return "admin/ad_active";


    }


    @GetMapping("/notice-update/{id}")
    public String noticeUpdatePage(@PathVariable("id") int id, Model model) {
        NoticeDTO dto = adminService.findNotice(id);

        model.addAttribute("notice", dto);
        return "/admin/admin_update_form";
    }

    @PostMapping("/notice-update/{id}")
    public String reviseNotice(@PathVariable("id") int id, NoticeDTO dto) {
        System.out.println(id);
        System.out.println(dto.getTitle());
        System.out.println(dto.getContent());

        adminService.updateNotice(id, dto);
        return "redirect:/admin/notice";
    }

    @DeleteMapping("/revision-notice/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable("id") int id) {
        adminService.deleteNotice(id);
        return ResponseEntity.ok("삭제 요청이 성공적으로 처리되었습니다.");
    }

    /**
     * 공지사항 작성하는 페이지
     *
     * @return
     */
    @GetMapping("/notice-creation")
    public String noticeAddPage() {

        return "/admin/notice_creating_form";
    }

    /**
     * 게시글 작성 폼
     *
     * @param title
     * @param content
     * @return
     */
    @PostMapping("/notice-creation")
    public String noticeAddProc(@RequestParam(name = "title") String title, @RequestParam(name = "content") String content) {
        adminService.createNotice(title, content);
        return "redirect:/admin/notice";
    }

    /**
     * 공지 자세히보기 띄우기
     *
     * @param id
     * @return
     */
    @GetMapping("/noticeModal/{id}")
    @ResponseBody
    public NoticeDTO noticeModalPage(@PathVariable("id") int id) {
        NoticeDTO dto = adminService.findNotice(id);
        return dto;
    }

    @GetMapping("/ad/save")
    public String savePage() {
        return "admin/ad_save_form";
    }

    @PostMapping("/ad/save")
    public String saveProc(@RequestParam("title") String title,
                           @RequestParam("originFileName") MultipartFile originFileName,
                           @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startTime,
                           @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endTime) {
        AdDTO dto = new AdDTO();
        String[] uploadedFileInfo = adminService.uploadFile(originFileName);
        dto.setOriginFileName(uploadedFileInfo[1]);
        dto.setTitle(title);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        adminService.createAd(dto);
        return "redirect:/admin/ad/list";
    }

    @GetMapping("/ad/list")
    public String adList(Model model, @PageableDefault(size = 2) Pageable pageable) {
        Page<Ad> adPage = adminService.getAdList(pageable);
        List<Ad> list = adPage.getContent();
        int currentPage = adPage.getNumber();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", adPage.getTotalPages()); // 전체 페이지 수 추가
        model.addAttribute("nextPage", currentPage + 1 < adPage.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", adPage);
        model.addAttribute("list", list);
        return "admin/ad_list";
    }

    @GetMapping("/ad/detail/{id}")
    public String adDetail(@PathVariable(name = "id") Integer id, Model model) {
        Ad ad = adminService.getAdDetail(id);
        model.addAttribute("ad", ad);
        model.addAttribute("title", ad.getTitle());
        model.addAttribute("originFileName", ad.getOriginFileName());
        model.addAttribute("startTime", ad.getStartTime());
        model.addAttribute("endTime", ad.getEndTime());
        return "admin/ad_detail";
    }

    @PostMapping("/ad/delete/{id}")
    public String deleteAd(@PathVariable(name = "id") Integer id) {
        adminService.deleteAd(id);
        return "redirect:/admin/ad/list";
    }

    @GetMapping("/ad/update/{id}")
    public String updateAdPage(@PathVariable(name = "id") int id, Model model) {
        Ad ad = adminService.getAdDetail(id);
        model.addAttribute("ad", ad);
        return "admin/ad_update";
    }

    @PostMapping("/ad/update/{id}")
    public String updateAd(@PathVariable(name = "id") Integer id, @RequestParam String title) {
        adminService.updateAdById(id, title);
        return "redirect:/admin/ad/list";
    }
}
