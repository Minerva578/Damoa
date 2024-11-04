
package com.damoa.repository.interfaces;

import java.util.List;

import com.damoa.dto.MonthlyFreelancerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.damoa.dto.freelancer.FreelancerBasicInfoDTO;
import com.damoa.repository.model.Career;
import com.damoa.repository.model.Freelancer;
import com.damoa.repository.model.Skill;

@Mapper
public interface FreelancerRepository {

    public int insertFreelancer(Freelancer freelancer); // 프리랜서 등록

    // 전체 프리랜서 조회
    public List<Freelancer> findAllFreelancers(@Param("offset") int offset, @Param("size") int size);
    
    List<Freelancer> findAllFreelancersBySearch(
        @Param("offset") int offset,
        @Param("size") int size,
        @Param("skill") String skill,
        @Param("workingStyle") String workingStyle,
        @Param("jobPart") String jobPart);

    public int countAllFreelancers(); // 전체 프리랜서 수 조회

    public int countAllFreelancersBySearch(
    @Param("skill") String skill,
    @Param("workingStyle") String workingStyle,
    @Param("jobPart") String jobPart);

    public Freelancer findUserIdJoinFreelancerTb(int id); // 유저 id로 프리랜서 조회

    // 프리랜서 기본정보 추가
    public int insertFreelancerBasicInfo(FreelancerBasicInfoDTO dto);

    public int updateFreelancerBasicInfo(FreelancerBasicInfoDTO dto);

    public FreelancerBasicInfoDTO findFreelancerBasicInfo(int userId);

    // 모든 경력 내용 조회
    public List<Career> findAllCareers(); 

    // 프리랜서 경력 추가
    public void insertFreelancerCareer(@Param("userId") int userId, @Param("careerId") int careerId);

    // 프리랜서 경력 조회
    public List<Career> findCareersByFreelancerId(@Param("userId") int userId);

    // 프리랜서 경력 삭제
    public void deleteFreelancerCareerByFreelancerId(@Param("userId") int userId, @Param("careerId") int careerId);

    // 모든 스킬 조회
    public List<Skill> findAllSkills();

    // 프리랜서 스킬 추가
    public void insertFreelancerSkill(@Param("userId") int userId, @Param("skillId") int skillId);

    // 프리랜서 스킬 조회
    public List<Skill> findSkillsByFreelancerId(@Param("userId") int userId);

    // 프리랜서 스킬 삭제
    public void deleteFreelancerSkillByFreelancerId(@Param("userId") int userId, @Param("skillId") int skillId);

    // 프리랜서 디테일 조회
    public Freelancer findFreelancerDetailById(int userId);

    // 프리랜서 평균 희망 연봉 카운트
    public int countAverageFreelancerExpectedSalary();

    // 월별 프리랜서 등록 수
    List<MonthlyFreelancerDTO> getMonthlyFreelancerData();
}
