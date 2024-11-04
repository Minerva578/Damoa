package com.damoa.service;

import com.damoa.dto.user.MonthlyVisitorDTO;
import com.damoa.repository.interfaces.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VisitorService {

    @Autowired
    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Transactional
    public void recordVisitor(String userIp) {
        // IP 주소가 null이거나 빈 문자열이 아닌지 확인
        if (userIp != null && !userIp.isEmpty()) {
            visitorRepository.insertVisitor(userIp); // IP를 DB에 기록
        } else {
            System.out.println("Invalid IP address: " + userIp);
        }
    }


    public List<MonthlyVisitorDTO> getMonthlyVisitorData() {
        return visitorRepository.getMonthlyVisitorData();
    }

}
