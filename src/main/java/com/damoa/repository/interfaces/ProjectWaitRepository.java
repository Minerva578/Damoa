package com.damoa.repository.interfaces;

import com.damoa.dto.user.ProjectWaitDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectWaitRepository {

    int isRegistered(@Param("userId") int userId, @Param("projectId")int projectId);

    void insertNewRegister(@Param("userId")int userId, @Param("projectId")int projectId);

    List<ProjectWaitDTO> selectAllWait(@Param("projectId")int projectId, @Param("userId")Integer userId);

    ProjectWaitDTO selectProjectWaitByFreelancerIdAndProjectId(@Param("freelancerId")int freelancerId, @Param("projectId")int projectId);

    void updateStatusById(@Param("projectId")int projectId, @Param("freelancerId")int freelancerId, @Param("status")int status);

    List<Integer> selectAllProjectByFreelancerId(@Param("freelancerId")int freelancerId, @Param("status")int status);

    void updateProjectWaitStatus(@Param("status")int status, @Param("freelancerId")int freelancerId, @Param("projectId")int projectId);
}
