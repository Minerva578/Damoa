package com.damoa.repository.interfaces;

import com.damoa.dto.MonthlyProjectDTO;
import com.damoa.dto.user.SelectDTO;
import com.damoa.repository.model.Project;
import com.damoa.repository.model.ProjectWait;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface ProjectRepository {
    public int updateById(Project project);

    public int deleteById(Project project);

    int insertProject(
            @Param("id") int id,
            @Param("userId") int userId,
            @Param("job") String job,
            @Param("minYears") int minYears,
            @Param("maxYears") int maxYears,
            @Param("projectName") String projectname,
            @Param("projectStart") LocalDate projectStart,
            @Param("expectedPeriod") String expectedPeriod,
            @Param("period") int period,
            @Param("salaryType") String salaryType,
            @Param("workingStyle") String workingStyle,
            @Param("meeting") String meeting,
            @Param("address") String address,
            @Param("worktime") String worktime,
            @Param("workAdjust") String workAdjust,
            @Param("vacation") String vacation,
            @Param("progress") byte[] progress,
            @Param("mainTasks") byte[] mainTasks,
            @Param("detailTask") byte[] detailTask,
            @Param("delivered") byte[] delivered,
            @Param("files") byte[] files,
            byte[] dtoFiles, @Param("company") String company,
            @Param("managerName") String managerName,
            @Param("contact") String contact,
            @Param("email") String email,
            @Param("agree") int agree
    );

    // 모든 프로젝트 가져오기
    List<Project> getAllProject();

    // 프로젝트 가져오기 (페이징
    List<Project> getProjectForPaging(@Param("limit")int limit, @Param("offset")int offset);

    // id로 프로젝트 가져오기
    Project getProjectById(int projectId);

    // 프로젝트 대기열 생성
    void addNewWait(ProjectWait reqDTO);

    // 월별 프로젝트 등록 수
    List<MonthlyProjectDTO> getMonthlyProjectData();

    // 프로젝트 명으로 프로젝트 검색하기
    List<Project> selectProjectByName(String keyword);

    // 마이페이지용 프로젝트 검색
    List<Project> getProjectForPagingForMyPage(@Param("userId")int userId, @Param("limit")int limit, @Param("offset")int offset);
    
    // 완료된 프로젝트 검색
    List<Project> getProjectForPagingForMyPage2(@Param("userId")int userId, @Param("limit")int limit, @Param("offset")int offset);

    // 광고받은 프로젝트들 검색
    List<Project> getProjectForAd(@Param("limit")int limit, @Param("offset")int offset);

    // 테스트
    int test(@Param("str")String str);

    // 검색 조건에 따라 검색
    List<Project> selectProjectForSelect(@Param("selectDTO")SelectDTO selectDTO, @Param("limit")int limit, @Param("offset")int offset);

    int selectProjectIdByUserId(int userId);

    void updateStatusById(int id);
}
