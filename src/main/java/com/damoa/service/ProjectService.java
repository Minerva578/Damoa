package com.damoa.service;

import com.damoa.dto.MonthlyProjectDTO;
import com.damoa.dto.ProjectSaveDTO;
import com.damoa.dto.user.ProjectWaitDTO;
import com.damoa.dto.user.SelectDTO;
import com.damoa.handler.exception.DataDeliveryException;
import com.damoa.repository.interfaces.ProjectRepository;
import com.damoa.repository.model.Project;
import com.damoa.repository.model.ProjectWait;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public void createProject(ProjectSaveDTO dto) {
        int result = 0;

        try {
            result = projectRepository.insertProject(dto.getUserId(), dto.getUserId(), dto.getJob(), dto.getMinYears(), dto.getMaxYears()
                    , dto.getProjectName(), dto.getProjectStart(), dto.getExpectedPeriod(), dto.getPeriod(), dto.getSalaryType(), dto.getWorkingStyle(), dto.getMeeting(), dto.getAddress()
                    , dto.getWorktime(), dto.getWorkAdjust(), dto.getVacation(), dto.getProgress(), dto.getMainTasks(), dto.getDetailTask(), dto.getDetailTask()
                    , dto.getDelivered(), dto.getFiles().getBytes(), dto.getCompany(), dto.getManagerName(), dto.getContact(), dto.getEmail(), dto.getAgree());
        } catch (DataDeliveryException e) {
            throw new DataDeliveryException("잘못된 요청입니다", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (result == 0) {
            throw new DataDeliveryException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 모든 프로젝트 가져오기
    public List<Project> getAllProject() {
        return projectRepository.getAllProject();
    }

    // 프로젝트 가져오기 (*페이징)
    public List<Project> getProjectForPaging(int limit, int offset) {
        return projectRepository.getProjectForPaging(limit, offset);
    }

    // id로 프로젝트 찾기
    public Project findProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

    // 프로젝트 대기열 추가
    public void makeNewWait(ProjectWait reqDTO) {
        projectRepository.addNewWait(reqDTO);
        return;
    }

    // 월별 프로젝트 등록 데이터
    public List<MonthlyProjectDTO> getMonthlyProjectData() {
        return projectRepository.getMonthlyProjectData();
    }

    /**
     * 프로젝트 명으로 프로젝트 검색하기
     *
     * @param keyword
     * @return
     */
    public List<Project> findByProjectName(String keyword) {
        return projectRepository.selectProjectByName(keyword);
    }

    /**
     * 마이페이지용 프로젝트 조회
     *
     * @param userId
     * @param limit
     * @param offset
     * @return
     */
    public List<Project> getProjectForPagingForMyPage2(int userId, int limit, int offset) {
        return projectRepository.getProjectForPagingForMyPage2(userId, limit, offset);
    }

    public List<Project> getProjectForPagingForMyPage(int userId, int limit, int offset) {
        return projectRepository.getProjectForPagingForMyPage(userId, limit, offset);
    }

    /**
     * 광고받은 프로젝트들 조회
     *
     * @param limit
     * @param offset
     * @return
     */
    public List<Project> getProjectForAdvertise(int limit, int offset) {
        return projectRepository.getProjectForAd(limit, offset);
    }

    /**
     * select 검색 설정
     *
     * @param reqDTO
     * @param limit
     * @param offset
     * @return
     */
    public List<Project> getProjectForSelect(SelectDTO reqDTO, int limit, int offset) {
        String selectStr = null;

            // 모든 항목 x 1111111
        if ((reqDTO.getProgress()==null || reqDTO.getProgress().equals("전체"))  && (reqDTO.getWorkType()==null|| reqDTO.getWorkType().equals("전체")) && (reqDTO.getAddress()==null|| reqDTO.getAddress().equals("전국"))) {
            System.out.println("1111111");
            List<Project> newProject = projectRepository.getProjectForPaging(limit,offset);
            System.out.println(selectStr);
            return newProject;
        }

        List<Project> newProject = projectRepository.selectProjectForSelect(reqDTO, limit, offset);

        return newProject;
    }


    public int findProjectIdByUserId(int userId) {
        return projectRepository.selectProjectIdByUserId(userId);
    }

    public void changeStatusById(int id) {
        projectRepository.updateStatusById(id);
        return;
    }
}