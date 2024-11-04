package com.damoa.repository.interfaces;

import com.damoa.dto.TossHistoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface PaymentHistoryRepository {
    // 결제 내역 데이터베이스에 추가
    int insertTossHistory(TossHistoryDTO dto);

    TossHistoryDTO findById(int id);

    List<TossHistoryDTO> viewAll(@Param("offset") int offset, @Param("pageSize") int pageSize);

    List<TossHistoryDTO> findHistoryByStatus(@Param("offset") int offset, @Param("pageSize") int pageSize);

    int findPointById(int userId);

    void updateUserPoint(@Param("amount") int amount, @Param("userId") int userId);

    void updateHistoryStatus(int id);

    void insertCancelHistory(TossHistoryDTO tossHistoryDTO);

    int countAll();

    int countRequestedRefund();
}
