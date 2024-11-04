
package com.damoa.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.damoa.dto.freelancer.FreelancerBasicInfoDTO;
import com.damoa.dto.user.UserSignUpDTO;
import com.damoa.repository.model.Career;
import com.damoa.repository.model.Freelancer;
import com.damoa.repository.model.Skill;
import com.damoa.repository.model.User;
import com.damoa.service.FreelancerService;
import com.damoa.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/freelancer")
public class FreelancerController {

    @Autowired
    private final FreelancerService freelancerService;

    /**
     * 근무 방식 및 연봉 페이지
     * 
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/basic-info")
    public String BasicInfoPage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");

        // 사용자가 로그인하지 않았을 경우 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        // 사용자가 프리랜서인지 확인
        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 프리랜서의 기본 정보 가져오기
        Freelancer freelancer = freelancerService.findUserIdJoinFreelancerTb(user.getId());
        model.addAttribute("isLogin", user);
        model.addAttribute("isFreelancer", user);
        model.addAttribute("freelancer", freelancer); // userId 추가
        return "freelancer/basic_info"; // 템플릿 반환
    }

    @GetMapping("/insert-basic-info")
    public String insertBasicInfoPage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");

        // 사용자가 로그인하지 않았을 경우 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        // 사용자가 프리랜서인지 확인
        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 모든 경력 조회
        List<Career> careers = freelancerService.findAllCareers();

        model.addAttribute("isLogin", user);
        model.addAttribute("isFreelancer", user);
        model.addAttribute("careers", careers);
        model.addAttribute("userId", user.getId()); // userId 추가
        return "freelancer/insert_basic_info"; // 템플릿 반환
    }

    
    @PostMapping("/insert-basic-info")
    public String insertBasicInfo(@ModelAttribute FreelancerBasicInfoDTO dto, UserSignUpDTO userSignUpDTO,
            HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");

        // 사용자가 로그인하지 않았을 경우 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        // 사용자가 프리랜서인지 확인
        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 업데이트 로직
        freelancerService.insertFreelancerBasicInfo(dto, userSignUpDTO);

        // 업데이트 완료 후 적절한 페이지로 리디렉션 (예: 기본 정보 페이지)
        return "redirect:/freelancer/basic-info";
    }

    /**
     * 근무 방식 및 연봉 업데이트 페이지
     * 
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/update-basic-info")
    public String updateBasicInfoPage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");

        // 사용자가 로그인하지 않았을 경우 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        // 사용자가 프리랜서인지 확인
        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 모든 경력 조회
        List<Career> careers = freelancerService.findAllCareers();

        // 프리랜서의 기본 정보 가져오기
        Freelancer freelancer = freelancerService.findUserIdJoinFreelancerTb(user.getId());
        if (freelancer != null) {
            model.addAttribute("careers", careers);
            model.addAttribute("jobPart", freelancer.getJobPart());
            model.addAttribute("workingStyle", freelancer.getWorkingStyle());
            model.addAttribute("expectedSalary", freelancer.getExpectedSalary());
            model.addAttribute("salaryType", freelancer.getSalaryType());
            model.addAttribute("detail", freelancer.getDetail());
            model.addAttribute("link", freelancer.getLink());
            model.addAttribute("freelancer", user.getUserType());
        }

        model.addAttribute("isLogin", user);
        model.addAttribute("isFreelancer", user);
        model.addAttribute("userId", user.getId()); // userId 추가
        return "freelancer/update_basic_info"; // 템플릿 반환
    }

    /**
     * 근무 방식 및 연봉 업데이트 로직
     * 
     * @param updateDTO
     * @param session
     * @return
     */
    @PostMapping("/update-basic-info")
    public String updateBasicInfo(@ModelAttribute FreelancerBasicInfoDTO dto, UserSignUpDTO userSignUpDTO,
            HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");

        // 사용자가 로그인하지 않았을 경우 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        // 사용자가 프리랜서인지 확인
        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 업데이트 로직
        freelancerService.updateFreelancerBasicInfo(dto, userSignUpDTO);

        // 업데이트 완료 후 적절한 페이지로 리디렉션 (예: 기본 정보 페이지)
        return "redirect:/freelancer/basic-info";
    }

    /**
     * 프리랜서의 경력 조회 페이지
     * 
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/career")
    public String findCareersPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("principal");

        if (user == null) {
            return "redirect:/user/sign-in";
        }

        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 모든 경력 조회
        List<Career> careers = freelancerService.findAllCareers();
        model.addAttribute("careers", careers);

        List<Career> freelancerCareers = freelancerService.findCareersByFreelancerId(user.getId());
        model.addAttribute("freelancerCareers", freelancerCareers);

        model.addAttribute("isLogin", user);
        model.addAttribute("isFreelancer", user);
        model.addAttribute("freelancer", user.getUserType());

        return "freelancer/career"; // 경력 관리 템플릿 반환
    }

    /**
     * 프리랜서 경력 추가
     * 
     * @param session
     * @param careerId
     * @return
     */
    @PostMapping("/insert-career")
    public String insertCareers(HttpSession session, @RequestParam("careerId") int careerId) {
        User user = (User) session.getAttribute("principal");

        if (user == null) {
            return "redirect:/user/sign-in";
        }

        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 프리랜서 경력 추가
        freelancerService.insertFreelancerCareer(user.getId(), careerId);

        // 경력 관리 페이지로 리디렉션
        return "redirect:/freelancer/career";
    }

    /**
     * 프리랜서 경력 삭제 로직
     * 
     * @param session
     * @param careerId
     * @return
     */
    @PostMapping("/delete-career")
    public String deleteCareers(HttpSession session, @RequestParam("careerId") int careerId) {
        User user = (User) session.getAttribute("principal");

        if (user == null) {
            return "redirect:/user/sign-in";
        }

        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 프리랜서 경력 삭제
        freelancerService.deleteFreelancerCareerByFreelancerId(user.getId(), careerId);

        // 경력 관리 페이지로 리디렉션
        return "redirect:/freelancer/career";
    }

    /**
     * 프리랜서 스킬 스택 조회 페이지
     */
    @GetMapping("/skills")
    public String findSkillsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("principal");

        if (user == null) {
            return "redirect:/user/sign-in";
        }

        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 모든 스킬 조회
        List<Skill> skills = freelancerService.findAllSkills();
        model.addAttribute("skills", skills);

        List<Skill> freelancerSkills = freelancerService.findSkillsByFreelancerId(user.getId());
        model.addAttribute("freelancerSkills", freelancerSkills);

        model.addAttribute("isLogin", user);
        model.addAttribute("isFreelancer", user);
        model.addAttribute("freelancer", user.getUserType());

        return "freelancer/skills"; // 스킬 관리 템플릿 반환

    }

    /**
     * 프리랜서 스킬 추가
     * 
     * @param session
     * @param skillId
     * @return
     */
    @PostMapping("/insert-skill")
    public String insertSkills(HttpSession session, @RequestParam("skillId") int skillId) {
        User user = (User) session.getAttribute("principal");

        if (user == null) {
            return "redirect:/user/sign-in";
        }

        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 프리랜서 스킬 추가
        freelancerService.insertFreelancerSkill(user.getId(), skillId);

        // 스킬 관리 페이지로 리디렉션
        return "redirect:/freelancer/skills";
    }

    /**
     * 프리랜서 스킬 삭제
     * 
     * @param session
     * @param skillId
     * @return
     */
    @PostMapping("/delete-skill")
    public String deleteSkills(HttpSession session, @RequestParam("skillId") int skillId) {
        User user = (User) session.getAttribute("principal");

        if (user == null) {
            return "redirect:/user/sign-in";
        }

        if (!"freelancer".equals(user.getUserType())) {
            throw new IllegalArgumentException("프리랜서만 접근할 수 있습니다.");
        }

        // 프리랜서 스킬 삭제
        freelancerService.deleteFreelancerSkill(user.getId(), skillId);

        // 스킬 관리 페이지로 리디렉션
        return "redirect:/freelancer/skills";
    }

    /**
     * 프리랜서 찾기 리스트 페이지
     * 
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String findFreelancerPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("principal");
        // 평균 급여 계산
        double averageSalary = freelancerService.countAverageFreelancerExpectedSalary();

        // 소수점 없이 처리
        DecimalFormat df = new DecimalFormat("#");
        String formattedSalary = df.format(averageSalary);

        // 모든 스킬 조회
        List<Skill> skills = freelancerService.findAllSkills();
        model.addAttribute("skills", skills);

        // 모든 직무 조회
        List<Career> careers = freelancerService.findAllCareers();
        model.addAttribute("careers", careers);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        model.addAttribute("isLogin", user);
        model.addAttribute("averageSalary", formattedSalary);
        return "/freelancer/freelancer_list";
    }

    /**
     * 프리랜서 목록 AJAX 요청을 처리할 엔드포인트
     * 
     * @param page
     * @param size
     * @param jobPart
     * @param workingStyle
     * @param skills
     * @return
     */
    @GetMapping("/list/data")
    public ResponseEntity<Map<String, Object>> fetchFreelancerList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "skill", required = false) String skill,
            @RequestParam(name = "workingStyle", required = false) String workingStyle,
            @RequestParam(name = "jobPart", required = false) String jobPart) {
    
        // 프리랜서 목록을 페이지별로 조회
        List<Freelancer> freelancers;
        if (skill != null && !skill.isEmpty()) {
            freelancers = freelancerService.findAllFreelancersBySearch(page, size, skill, workingStyle, jobPart);
        } else if (jobPart != null && !jobPart.isEmpty()) {
            freelancers = freelancerService.findAllFreelancersBySearch(page, size, skill, workingStyle, jobPart);
        } else if (workingStyle != null && !workingStyle.isEmpty()) {
            freelancers = freelancerService.findAllFreelancersBySearch(page, size, skill, workingStyle, jobPart);
        } else {
            freelancers = freelancerService.findAllFreelancers(page, size); // 전체 조회
        }
        int totalFreelancers = freelancerService.countAllFreelancersBySearch(skill, workingStyle, jobPart);
        int totalPages = (int) Math.ceil((double) totalFreelancers / size);
    
        // 프리랜서 평균 희망 연봉도 함께 응답 데이터에 추가
        int averageSalary = freelancerService.countAverageFreelancerExpectedSalary();
    
        System.out.println("Received parameters: page=" + page + ", size=" + size + ", job=" + jobPart + ", workingStyle=" + workingStyle + ", skill=" + skill);

        // 응답 데이터
        Map<String, Object> response = new HashMap<>();
        response.put("freelancers", freelancers);
        response.put("totalCount", totalFreelancers);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalPages", totalPages);
        response.put("averageSalary", averageSalary);
    
        return ResponseEntity.ok(response); // JSON 형태로 응답
    }

    /**
     * 프리랜서 상세보기
     * @param userId
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/detail/{userId}")
    public String freelancerDetailPage(@PathVariable("userId") int userId, HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("principal");
        // 사용자가 로그인하지 않았을 경우 로그인 페이지로 리디렉션
        if (user == null) {
            return "redirect:/user/sign-in";
        }

        // 프리랜서 상세 정보 조회
        Freelancer freelancer = freelancerService.findFreelancerDetailById(userId);

        // 프리랜서가 없을 경우 예외 처리
        if (freelancer == null) {
            throw new IllegalArgumentException("해당 프리랜서가 존재하지 않습니다.");
        }

        // 모델에 프리랜서 정보 추가
        model.addAttribute("freelancer", freelancer);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        model.addAttribute("isLogin", user);

        return "freelancer/detail";
    }
}