package com.damoa.service;

import com.damoa.repository.interfaces.SkillRepository;
import com.damoa.repository.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;
    public List<Skill> getAllSkill(){
        return skillRepository.selectAllSkill();
    }

    /**
     * 유저가 입력한 정보를 기반으로 skill List 반환
     * @param strList
     * @return
     */
    public List<Skill> findSkillListByName(List<String> strList) {
        List<Skill> skillList = new ArrayList<>();
        for(int i=0; i<strList.size(); i++){
            Skill skill = skillRepository.selectSkillsByName(strList.get(i));
            skillList.add(skill);
        };
        return skillList;
    }

    /**
     * skill List를 통해 prokect_skill_tb를 생성
     * @param skillList
     */
    public void addProjectSkillData(int companyId, int projectId, List<Skill> skillList) {
        int size = skillList.size();
        for(int i=0; i<size; i++){
            skillRepository.addProjectSkillData(projectId,companyId,skillList.get(i).getId());
        }
    }

    /**
     * 프로젝트 id로 skill list 찾기
     * @param projectId
     * @return
     */
    public List<String> findSkillsByProjectId(int projectId){
        return skillRepository.findProjectSkillById(projectId);
    }
}
