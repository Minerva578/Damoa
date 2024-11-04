package com.damoa.service;

import com.damoa.dto.user.ProjectWaitDTO;
import com.damoa.repository.interfaces.ProjectWaitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectWaitService {

    @Autowired
    public ProjectWaitRepository projectWaitRepository;

    // userId / projectId로 신청 내역 확인하기
    public int isRegistered(int userId, int projectId){
        return projectWaitRepository.isRegistered(userId,projectId);
    }

    // 새로운 매칭 대기 정보 추가
    public void addNewWait(int userId, int projectId) {
        projectWaitRepository.insertNewRegister(userId,projectId);
        return;
    }

    // 모든 대기 정보 불러오기 (프로젝트 id, 컴퍼니 id)
    public List<ProjectWaitDTO> getAllWaitByProjectAndWriterId(int projectId, Integer companyId) {
        return projectWaitRepository.selectAllWait(projectId,companyId);
    }

    // 프리랜서 id+프로젝트 id로 대기 정보 불러오기
    public ProjectWaitDTO getProjectWaitByFreelancerIdAndProjectId(int freelancerId, int projectId){
        return projectWaitRepository.selectProjectWaitByFreelancerIdAndProjectId(freelancerId, projectId);
    }

    // 스테이터스 변경하기
    public void changeStatusById(int projectId, int freelancerId,int status) {
        projectWaitRepository.updateStatusById(projectId,freelancerId,status);
        return;
    }

    // 프리랜서가 신청한 모든 프로젝트 정보 불러오기
    public List<Integer> getAllProjectByFreelacnerId(int freelancerId, int status){
        return projectWaitRepository.selectAllProjectByFreelancerId(freelancerId,status);
    }

    public void setProjectWaitStatus(int status, int freelancerId, int projectId){
        projectWaitRepository.updateProjectWaitStatus(status,freelancerId,projectId);
        return;
    }
}
