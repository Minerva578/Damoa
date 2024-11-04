
package com.damoa.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.damoa.dto.MonthlyFreelancerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.damoa.dto.freelancer.FreelancerBasicInfoDTO;
import com.damoa.dto.freelancer.RegisterFreelancerDTO;
import com.damoa.dto.user.UserSignUpDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.FreelancerRepository;
import com.damoa.repository.interfaces.SkillRepository;
import com.damoa.repository.interfaces.UserRepository;
import com.damoa.repository.model.Career;
import com.damoa.repository.model.Freelancer;
import com.damoa.repository.model.Skill;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    @Autowired
    private final FreelancerRepository freelancerRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final SkillRepository skillRepository;

    /**
     * 파일 업로드
     */
    @Transactional
    public String[] uploadFile(MultipartFile mFile) {
        if (mFile.getSize() > 1024 * 1024 * 20) {
            throw new DataDeliveryException("파일 크기는 20MB 이상 클 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String saveDirectory = null;

        try {
            saveDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/portfolio/";
            System.out.println("saveDirectory : " + saveDirectory);
            File dir = new File(saveDirectory);
            if (!dir.exists()) {
                dir.mkdirs(); // 디렉토리 생성
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String originalFileName = mFile.getOriginalFilename();
        String uploadFileName = UUID.randomUUID() + "_" + originalFileName;
        String uploadPath = saveDirectory + File.separator + uploadFileName;
        File destination = new File(uploadPath);
        System.out.println("UPLOADPATH : " + uploadPath);
        System.out.println("destination : " + destination);
        try {
            mFile.transferTo(destination);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            throw new DataDeliveryException("파일 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new String[] { originalFileName, uploadFileName };
    }

    /**
     * 프리랜서 리스트 조회
     * 
     * @return
     */
    public List<Freelancer> findAllFreelancers(int page, int size) {
        int offset = (page - 1) * size;
        return freelancerRepository.findAllFreelancers(offset, size);
    }

    public List<Freelancer> findAllFreelancersBySearch(int page, int size, String skill, String workingStyle, String jobPart) {
        int offset = (page - 1) * size;
        
        // 필터가 null이거나 빈 문자열일 경우 처리
        if (skill == null || skill.isEmpty()) {
            skill = null;
        }
        if (workingStyle == null || workingStyle.isEmpty()) {
            workingStyle = null;
        }
        if (jobPart == null || jobPart.isEmpty()) {
            jobPart = null;
        }
    
        return freelancerRepository.findAllFreelancersBySearch(offset, size, skill, workingStyle, jobPart);
    }

    /**
     * 프리랜서 리스트 카운트
     * 
     * @return
     */
    public int countAllFreelancers() {
        return freelancerRepository.countAllFreelancers();
    }

    public int countAllFreelancersBySearch(String skill, String workingStyle, String jobPart) {
        return freelancerRepository.countAllFreelancersBySearch(skill, workingStyle, jobPart);
    }

    /**
     * userId로 프리랜서 기본 정보 조회
     * 
     * @param id
     * @return
     */
    public Freelancer findUserIdJoinFreelancerTb(int id) {
        return freelancerRepository.findUserIdJoinFreelancerTb(id);
    }

    /**
     * 프리랜서 기본 정보 인서트
     * 
     * @param dto
     */
    @Transactional
    public void insertFreelancerBasicInfo(FreelancerBasicInfoDTO dto, UserSignUpDTO user) {
        // 파일 업로드 수행
        if (dto.getMFile() != null && !dto.getMFile().isEmpty()) {
            String[] fileNames = uploadFile(dto.getMFile());
            dto.setOriginFileName(fileNames[0]);
            dto.setUploadFileName(fileNames[1]);
        }

        // 회원가입한 사용자가 프리랜서일 경우, freelancer_tb에 자동 등록
        if ("freelancer".equals(user.getUserType())) {
            // userRepository에서 방금 삽입된 유저의 id 가져오기
            int userId = userRepository.findByEmail(user.getEmail()).getId();

            // 기본 프리랜서 정보를 추가 (기본값 설정)
            Freelancer freelancer = new Freelancer();
            freelancer.setUserId(userId);
            // 나머지 값은 필요에 따라 기본값으로 설정하거나 초기 값으로 설정 가능
            freelancerRepository.insertFreelancer(freelancer);
        }

        // 프리랜서 기본 정보 업데이트
        freelancerRepository.insertFreelancerBasicInfo(dto);
    }

    /**
     * 프리랜서 기본정보 업데이트
     * @param dto
     * @param user
     */
    @Transactional
    public void updateFreelancerBasicInfo(FreelancerBasicInfoDTO dto, UserSignUpDTO user) {
        // 파일 업로드 수행
        if (dto.getMFile() != null && !dto.getMFile().isEmpty()) {
            String[] fileNames = uploadFile(dto.getMFile());
            dto.setOriginFileName(fileNames[0]);
            dto.setUploadFileName(fileNames[1]);
        }

        // 회원가입한 사용자가 프리랜서일 경우, freelancer_tb에 자동 등록
        if ("freelancer".equals(user.getUserType())) {
            // userRepository에서 방금 삽입된 유저의 id 가져오기
            int userId = userRepository.findByEmail(user.getEmail()).getId();

            // 기본 프리랜서 정보를 추가 (기본값 설정)
            Freelancer freelancer = new Freelancer();
            freelancer.setUserId(userId);
        }

        // 프리랜서 기본 정보 업데이트
        freelancerRepository.updateFreelancerBasicInfo(dto);
    }

    /**
     * 프리랜서 기본 정보 find
     * 
     * @param userId
     * @return
     */
    public FreelancerBasicInfoDTO findFreelancerBasicInfo(int userId) {
        return freelancerRepository.findFreelancerBasicInfo(userId);
    }

    /**
     * 경력 정보 리스트 조회
     * 
     * @return
     */
    public List<Career> findAllCareers() {
        return freelancerRepository.findAllCareers();
    }

    /**
     * 프리랜서 경력 추가
     * 
     * @param userId
     * @param careerId
     */
    @Transactional
    public void insertFreelancerCareer(int userId, int careerId) {
        freelancerRepository.insertFreelancerCareer(userId, careerId);
    }

    /**
     * 프리랜서 경력 삭제
     * 
     * @param userId
     * @param careerId
     */
    @Transactional
    public void deleteFreelancerCareerByFreelancerId(int userId, int careerId) {
        freelancerRepository.deleteFreelancerCareerByFreelancerId(userId, careerId);
    }

    /**
     * 프리랜서 경력 조회
     * 
     * @param userId
     * @return
     */
    public List<Career> findCareersByFreelancerId(int userId) {
        return freelancerRepository.findCareersByFreelancerId(userId);
    }

    /**
     * 모든 스킬 가져오기
     * 
     * @return
     */
    public List<Skill> findAllSkills() {
        return freelancerRepository.findAllSkills();
    }

    /**
     * 프리랜서 스킬 조회
     * 
     * @param userId
     * @return
     */
    public List<Skill> findSkillsByFreelancerId(int userId) {
        return freelancerRepository.findSkillsByFreelancerId(userId);
    }

    /**
     * 프리랜서 스킬 추가
     * 
     * @param userId
     * @param skillId
     */
    @Transactional
    public void insertFreelancerSkill(int userId, int skillId) {
        freelancerRepository.insertFreelancerSkill(userId, skillId);
    }

    /**
     * 프리랜서 스킬 삭제
     * 
     * @param userId
     * @param skillId
     */
    @Transactional
    public void deleteFreelancerSkill(int userId, int skillId) {
        freelancerRepository.deleteFreelancerSkillByFreelancerId(userId, skillId);
    }

    /**
     * 프리랜서 상세 정보 조회
     */
    public Freelancer findFreelancerDetailById(int userId) {
        return freelancerRepository.findFreelancerDetailById(userId);
    }

    /**
     * 프리랜서 평균 희망 연봉 조회
     * 
     * @return
     */
    public int countAverageFreelancerExpectedSalary() {
        return freelancerRepository.countAverageFreelancerExpectedSalary();
    }

    /*
     * 월별 프리랜서 등록 수 데이터
     * @return
     */
    public List<MonthlyFreelancerDTO> getMonthlyFreelancerData() {
        return freelancerRepository.getMonthlyFreelancerData();
    }
}
