package com.damoa.repository.interfaces;

import com.damoa.repository.model.Skill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkillRepository {
    
    // 기술 스택 이름을 id로 조회
    public int findSkillIdByName(@Param("skill") String skill);

    public Skill selectSkillsByName(String name);

    // 기술 모두 가져오기
    public List<Skill> selectAllSkill();

    // Project_skill_tb에 데이터 추가
    public void addProjectSkillData(@Param("projectId")int projectId, @Param("userId")int userId, @Param("skill")int skill);

    // 프로젝트 id로 스킬내역 찾기
    public List<String> findProjectSkillById(Integer projectId);
}
