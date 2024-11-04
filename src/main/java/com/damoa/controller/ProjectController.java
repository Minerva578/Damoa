package com.damoa.controller;

import com.damoa.dto.ProjectSaveDTO;
import com.damoa.dto.freelancer.FreelancerBasicInfoDTO;
import com.damoa.dto.user.PrincipalDTO;
import com.damoa.dto.user.ProjectListDTO;
import com.damoa.dto.user.ProjectWaitDTO;
import com.damoa.dto.user.SelectDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.model.*;
import com.damoa.service.*;
import com.damoa.utils.Define;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private  HttpSession session;
    @Autowired
    private  ProjectService projectService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectWaitService projectWaitService;
    @Autowired
    private FreelancerService freelancerService;

    // DI
    public ProjectController(HttpSession session, ProjectService projectService) {
        this.session = session;
        this.projectService = projectService;
    }

    /**
     * 프로젝트 작성 폼 이동
     * @return
     */
    @GetMapping("/save")
    public String projectSavePage(Model model){
        User user = (User) session.getAttribute("principal");
        model.addAttribute("isLogin",user);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        List<Skill> skillList = skillService.getAllSkill();
        model.addAttribute("skillList",skillList);

        return "project/save_form";

    }

    /**
     * 프로젝트 작성 요청
     * @param reqDTO
     * @return
     */
    @PostMapping("/save")
    public String projectSaveProc(@ModelAttribute("reqDTO") ProjectSaveDTO reqDTO){


        
        projectService.createProject(reqDTO);
        int projectId = projectService.findProjectIdByUserId(1);
        
        userService.updateUserPoints(reqDTO.getUserId());
        List<String> strList=new ArrayList<>();
        List<Skill> skillList=new ArrayList<>();
        for(int a=0; a<reqDTO.getTotalSkills().size(); a++){
            String aa=reqDTO.getTotalSkills().get(a);
            String skill = aa.replaceAll("[^\\w.#/]", "");
            strList.add(skill);
        }

        int userId = 1;
        skillList = skillService.findSkillListByName(strList);
        skillService.addProjectSkillData(userId, projectId, skillList);

        return "project/save_complete";
    }

    /**
     * 프로젝트 게시판 이동
     * @param currentPageNum
     * @param model
     * @return
     */
    @GetMapping("/list/{currentPageNum}")
    public String projectListPage(@PathVariable(name="currentPageNum", required=false)int currentPageNum, Model model){

        User user = (User) session.getAttribute("principal");
        model.addAttribute("isLogin",user);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }

        // 모든 프로젝트 가져오기
        List<Project> projectList = projectService.getAllProject();
        int totalProjectNum = projectList.size();

        // 페이징 처리
        // limit - 한 페이지에 몇 개의 프로젝트가 들어갈 건가?
        int limit =10;

        // 총 페이지 수
        int totalPageNum = totalProjectNum/limit;

        // offset - 몇 번째 프로젝트부터 볼 것인가
        int offset;
        offset=limit*(currentPageNum-1);
        // 페이징 처리 된 프로젝트들
        List<Project> projectListForPaging = projectService.getProjectForPaging(limit,offset);

        List<ProjectListDTO> newList = new ArrayList<>();
        for(int i=0; i<projectListForPaging.size(); i++){
            ProjectListDTO dto = toProjectListDTO(projectListForPaging.get(i));
            newList.add(dto);
        }

        model.addAttribute("totalPageNum",totalPageNum);
        model.addAttribute("totalProjectNum",totalProjectNum);
        model.addAttribute("currentPageNum",currentPageNum);
        model.addAttribute("projectListForPaging",newList);

        return "project/list";
    }

    public ProjectListDTO toProjectListDTO(Project project) {
        ProjectListDTO newProDTO = new ProjectListDTO();
        newProDTO.setId(project.getId()); // id
        newProDTO.setUserName(userService.findUserById(project.getUserId()).getUsername()); // 유저 닉네임

        // 직무
        switch (project.getJob()) {
            case "1":
                newProDTO.setJob("풀스택");
                break;
            case "2":
                newProDTO.setJob("프론트엔드");
                break;
            case "3":
                newProDTO.setJob("백엔드");
                break;
            case "4":
                newProDTO.setJob("서버");
                break;
            case "5":
                newProDTO.setJob("데브옵스");
                break;
            case "6":
                newProDTO.setJob("데이터");
                break;
            case "7":
                newProDTO.setJob("AI");
                break;
            case "8":
                newProDTO.setJob("임베디드");
                break;
            case "9":
                newProDTO.setJob("미들웨어");
                break;
            case "10":
                newProDTO.setJob("웹퍼블리싱");
                break;
            default:
                newProDTO.setJob("알 수 없음");
        }

        newProDTO.setSkill(skillService.findSkillsByProjectId(project.getId())); // 스킬 목록 찾아오기
        newProDTO.setProjectName(project.getProjectName()); // 프로젝트 명
        newProDTO.setRequireYears("최소 " + project.getMinYears() + "년 ~ 최대 " + project.getMaxYears() + "년"); // 요구 연차
        newProDTO.setProjectStart(formatDate(project.getProjectStart())); // 프로젝트 시작일

        // 프로젝트 예상 기간 - 주/월
        newProDTO.setPeriod(project.getExpectedPeriod().equals("months") ? project.getPeriod() + "개월 예상" : project.getPeriod() + "주 예상");

        // 프로젝트 종류 - 기간제 / 프로젝트 단위
        newProDTO.setProjectType(project.getSalaryType().equals("month") ? "기간제" : "프로젝트 단위");

        // 근무 방식
        switch (project.getWorkingStyle()) {
            case "1":
                newProDTO.setWorkingStyle("원격 근무");
                break;
            case "2":
                newProDTO.setWorkingStyle("상주 근무");
                break;
            default:
                newProDTO.setWorkingStyle("원격/상주 모두 가능");
        }

        newProDTO.setMeeting(project.getMeeting().equals("1") ? "오프라인 미팅" : "온라인 미팅");

        switch (project.getProgress()) {
            case "1":
                newProDTO.setProgress("기획 단계");
                break;
            case "2":
                newProDTO.setProgress("기획서가 작성되어 있음");
                break;
            default:
                newProDTO.setProgress("모든 문서가 준비되어 있음");
        }

        newProDTO.setMainTasks(project.getMainTasks());
        newProDTO.setDetailTask(project.getDetailTask());
        newProDTO.setDelivered(project.getDelivered());
        newProDTO.setCompany(project.getCompany());
        newProDTO.setManagerName(project.getManagerName());
        newProDTO.setContact(project.getContact());
        newProDTO.setEmail(project.getEmail());
        newProDTO.setFiles(project.getFiles());
        newProDTO.setCreatedAt(formatTimestampToString(project.getCreatedAt()));

        return newProDTO;
    }

    public String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
        return formatter.format(date);
    }

    public String formatTimestampToString(Timestamp timestamp) {
        LocalDate date = timestamp.toLocalDateTime().toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"); // "년월일" 형식
        return date.format(formatter);
    }

    /**
     * 프로젝트 상세 보기
     * @param projectId
     * @param model
     * @return
     */
    @GetMapping("/detail/{projectId}")
    public String projectDetailPage(@PathVariable(name="projectId",required=false) int projectId,
                                    Model model,
                                    HttpSession session) {
        // Pair 1. 1:1 채팅 신청을 위한 data
        User user = (User) session.getAttribute("principal");
        model.addAttribute("session",user);

        // Project -> ProjectDTO
        Project project = projectService.findProjectById(projectId);
        ProjectListDTO dto = toProjectListDTO(project);

        // 헤더 값 추가
        if (user != null) {
            model.addAttribute("isLogin",user);
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
            if(user.getId() == project.getUserId()){
                List<ProjectWaitDTO> waitList = projectWaitService.getAllWaitByProjectAndWriterId(project.getId(),user.getId());
                model.addAttribute("waitList",waitList);
            }
        }

        // 만약 작성자라면 대기 정보 받아오기


        model.addAttribute("project",dto);
        model.addAttribute("projectInfo",project);
        System.out.println("~~~~~~~~");
        System.out.println(project);
        // Pair 2.
        model.addAttribute("comapnyId", project.getUserId());
        return "project/detail";
    }
//
//    @PostMapping("/wait")
//    public String projectWaitProc(@ModelAttribute ProjectWaitDTO reqDTO, Model model){
//        System.out.println(reqDTO);
//
//        ProjectWait newProWait = reqDTO.toProWait(reqDTO);
//
//        if(reqDTO.getFile() != null){
//            // 포트폴리오 파일 저장
//            String[] fileNames = uploadFile(reqDTO.getFile());
//            newProWait.setOriginFileName(fileNames[0]);
//            newProWait.setUploadFileName(fileNames[1]);
//        }
//
//        projectService.makeNewWait(newProWait);
//        System.out.println(newProWait);
//
//        Project project = projectService.findProjectById(reqDTO.getProjectId());
//        model.addAttribute("project",project);
//        return "project/detail";
//    }

    /**
     * 파일 업로드 및 이름 암호화
     * @param mFile
     * @return
     */
    private String[] uploadFile(MultipartFile mFile){
        if(mFile.getSize()> Define.MAX_FILE_SIZE){
            throw new DataDeliveryException("파일 크기는 20MB보다 클 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 서버 컴퓨터 - 디렉토리 체크
        String saveDirectory = Define.UPLOAD_FILE_DIRECTORY_FOR_PORTFOLIO;
        File directory = new File(saveDirectory);
        if(!directory.exists()){
            directory.mkdirs();
        }

        // 파일명 중복 방지를 위한 파일명
        String uploadFileName = UUID.randomUUID()+"_"+mFile.getOriginalFilename();

        // 파일 전체 경로 + 새로 생성한 파일명
        String uploadPath = saveDirectory+uploadFileName;
        File destination = new File(uploadPath);

        try {
            mFile.transferTo(destination);
        } catch (IllegalStateException | IOException e){
            e.printStackTrace();
            throw new DataDeliveryException("파일 업로드 중에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new String[] {mFile.getOriginalFilename(), uploadFileName};
    }

    /**
     * 나의 프로젝트
     * @param currentPageNum
     * @param model
     * @return
     */
    @GetMapping("/my-project")
    private String myProjectPage(@PathVariable(name="currentPageNum",required=false)Integer currentPageNum, Model model){
        int limit=10;
        if(currentPageNum==null || currentPageNum==0){
            currentPageNum=1;
        }
        int offset=limit*(currentPageNum-1);

        User user = (User) session.getAttribute("principal");
        model.addAttribute("session",user);

        List<Project> projectListForPaging = projectService.getProjectForPagingForMyPage(user.getId(),limit,offset);

        model.addAttribute("isLogin",user);
        model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
        model.addAttribute("isCompany", user.getUserType().equals("company"));
        model.addAttribute("projectListForPaging",projectListForPaging);
        return "user/my_project";
    }

    /**
     * 나의 프로젝트 (완료)
     * @param currentPageNum
     * @param model
     * @return
     */
    @GetMapping("/my-project/finished")
    private String myFinishedProjectPage(@PathVariable(name="currentPageNum",required=false)Integer currentPageNum, Model model){
        int limit=10;
        if(currentPageNum==null || currentPageNum==0){
            currentPageNum=1;
        }
        int offset=limit*(currentPageNum-1);

        User user = (User) session.getAttribute("principal");
        model.addAttribute("session",user);

        List<Project> projectListForPaging2 = projectService.getProjectForPagingForMyPage2(user.getId(),limit,offset);

        model.addAttribute("projectListForPaging2",projectListForPaging2);

        model.addAttribute("isLogin",user);
        model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
        model.addAttribute("isCompany", user.getUserType().equals("company"));
        return "user/my_project_finished";
    }


    @ResponseBody
    @PostMapping("/send-fetched-data")
    private List<Project> sendFetchedDataProc(@RequestBody SelectDTO select){

        // 페이징을 위한 전체 리스트 뽑아오기
        List<Project> projectList = projectService.getAllProject();
        int totalProjectNum = projectList.size();

        // 페이징 처리
        // limit - 한 페이지에 몇 개의 프로젝트가 들어갈 건가?
        int limit =10;
        // 총 페이지 수
        int totalPageNum = totalProjectNum/limit;

        // offset - 몇 번째 프로젝트부터 볼 것인가
        int offset=0;
        // 페이징 처리 된 프로젝트들
        List<Project> projectListForSelect = projectService.getProjectForSelect(select, limit,offset);

        return projectListForSelect;
    }

    /**
     * 새로운 대기 내역 추가
     * @param request
     * @return
     */
    @PostMapping("/make-new-wait")
    public ResponseEntity<Map<String, Integer>> makeNewWaitProc(@RequestBody Map<String, String> request) {
        int userId = Integer.parseInt(request.get("userId"));
        int projectId = Integer.parseInt(request.get("projectId"));
        int result = 0;


        int isRegistered = projectWaitService.isRegistered(userId,projectId);
        if(isRegistered == 0){
            // 신청 내역이 없는 경우에는 신청하기
            projectWaitService.addNewWait(userId,projectId);
        }  else {
            // 신청 내역이 있는 경우에는 return
            result = 1;
        }
        return ResponseEntity.ok(Collections.singletonMap("result", result)); // 성공
    }

    /**
     * 공고 마감하기
     * @param dto
     * @return
     */
    @PostMapping("/finish-project")
    public String finishProjectProc(@ModelAttribute ProjectWaitDTO dto){

        System.out.println("~~~~~~~");
        System.out.println(dto);

        // 1. 프로젝트 정보, 프로젝트 대기 정보 가져오기
        Project project = projectService.findProjectById(dto.getProjectId());
        ProjectWaitDTO waitInfo = projectWaitService.getProjectWaitByFreelancerIdAndProjectId(dto.getFreelancerId(),project.getId());

        // 2. 프로젝트, 프로젝트 대기 - status 변경
        projectService.changeStatusById(dto.getProjectId());
        projectWaitService.changeStatusById(waitInfo.getProjectId(), waitInfo.getFreelancerId(),2);

        // 3. 선택되지 않은 프로젝트 대기-status 변경
        projectWaitService.setProjectWaitStatus(3, waitInfo.getFreelancerId(), project.getId());

        return "redirect:/project/detail/"+project.getId();

    }

    /**
     * 신청 중인 프로젝트 페이지 - 프리랜서
     * @param model
     * @return
     */
    @GetMapping("/freelancer/my-project")
    private String freelancerProgressProjectPage(Model model){
        User user = (User) session.getAttribute("principal");
        model.addAttribute("session",user);

        // 유저 정보를 기반으로 프리랜서 정보 가져오기
        PrincipalDTO freelancerInfo = userService.findUserById(user.getId());
        System.out.println("userID _ : " + freelancerInfo);
        // 프리랜서 id로 대기 중(마감x) 정보 가져오기
        List<Integer> waitList = projectWaitService.getAllProjectByFreelacnerId(freelancerInfo.getId(),1);

        // 대기 중인 정보들을 기반으로 프로젝트 정보들 불러오기
        List<Project> projectList = new ArrayList<>();
        for(int i=0; i<waitList.size(); i++){
            Project newPro = projectService.findProjectById(waitList.get(i));
            projectList.add(newPro);
        }

        model.addAttribute("projectList",projectList);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        model.addAttribute("isLogin", user);
        return "/freelancer/my_project_on_progress";
    }

    /**
     * 신청 중인 프로젝트 페이지 - 프리랜서
     * @param model
     * @return
     */
    @GetMapping("/freelancer/my-project/finished")
    private String freelancerFinishedProjectPage(Model model){
        User user = (User) session.getAttribute("principal");
        model.addAttribute("session",user);

        // 유저 정보를 기반으로 프리랜서 정보 가져오기
        PrincipalDTO freelancerInfo = userService.findUserById(user.getId());
        // 프리랜서 id로 대기 중(마감x) 정보 가져오기
        List<Integer> waitList1 = projectWaitService.getAllProjectByFreelacnerId(freelancerInfo.getId(),2);
        List<Integer> waitList2 = projectWaitService.getAllProjectByFreelacnerId(freelancerInfo.getId(),3);

        // 대기 중인 정보들을 기반으로 프로젝트 정보들 불러오기
        List<Project> projectList1 = new ArrayList<>();
        List<Project> projectList2 = new ArrayList<>();
        for(int i=0; i<waitList1.size(); i++){
            Project newPro = projectService.findProjectById(waitList1.get(i));
            projectList1.add(newPro);
        }

        for(int i=0; i<waitList2.size(); i++){
            Project newPro = projectService.findProjectById(waitList2.get(i));
            projectList2.add(newPro);
        }

        model.addAttribute("projectList1",projectList1);
        model.addAttribute("projectList2",projectList2);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        model.addAttribute("isLogin", user);
        return "freelancer/my_project_finished";
    }

}