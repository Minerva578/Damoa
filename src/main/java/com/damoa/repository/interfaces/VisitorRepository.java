package com.damoa.repository.interfaces;

import com.damoa.dto.user.MonthlyVisitorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VisitorRepository {

    // 방문자 데이터를 기록하는 메서드
    void insertVisitor(@Param("userIp") String userIp);

    // 월별 방문자 수 데이터를 가져오는 메서드
    List<MonthlyVisitorDTO> getMonthlyVisitorData();
}
