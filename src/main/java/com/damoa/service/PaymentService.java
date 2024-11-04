package com.damoa.service;

import com.damoa.dto.TossApproveResponse;
import com.damoa.dto.TossHistoryDTO;
import com.damoa.repository.interfaces.PaymentHistoryRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private final PaymentHistoryRepository paymentHistoryRepository;

    public String getOrderId() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DATE);
        Random random1 = new Random();
        Random random2 = new Random();
        int rdValue1 = random1.nextInt(100);
        int rdValue2 = random1.nextInt(100);

        // 랜덤아이디를 만들기 위해 인트를 스트링으로 변환
        String yStr = Integer.toString(y);
        String mStr = Integer.toString(m);
        String dStr = Integer.toString(d);
        String randomStr1 = Integer.toString(rdValue1);
        String randomStr2 = Integer.toString(rdValue2);

        return mStr + randomStr1 + yStr + randomStr2 + dStr;
    }


    @Transactional
    public void insertTossHistory(TossApproveResponse response, int principalId) {
        int result = 0;
        TossHistoryDTO dto = TossHistoryDTO.builder()
                .paymentKey(response.getPaymentKey())
                .userId(principalId)
                .orderId(response.getOrderId())
                .orderName(response.getOrderName())
                .amount(response.getTotalAmount())
                .method(response.getMethod())
                .requestedAt(response.getRequestedAt())
                .approvedAt(response.getApprovedAt())
                .build();
        result = paymentHistoryRepository.insertTossHistory(dto);


        if (result != 1) {
            System.out.println("결제 실패 했어요..");
        }

    }

    public TossHistoryDTO findPaymentHistory(int id) {

        TossHistoryDTO entity = paymentHistoryRepository.findById(id);

        return entity;
    }

    public Page<TossHistoryDTO> findAll(Pageable pageable) {

        int offset = pageable.getPageNumber() * pageable.getPageSize();

        List<TossHistoryDTO> paymentList = paymentHistoryRepository.viewAll(offset, pageable.getPageSize());
        int totalCount = paymentHistoryRepository.countAll();
        return new PageImpl<>(paymentList, pageable, totalCount);
    }

    @Transactional
    public void updatePoint(String amountStr, Integer id) {

        int currentPoint = paymentHistoryRepository.findPointById(id);
        int amount = currentPoint + Integer.parseInt(amountStr);
        paymentHistoryRepository.updateUserPoint(amount, id);
    }

    public Page<TossHistoryDTO> findRequestedRefund(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<TossHistoryDTO> paymentList = paymentHistoryRepository.findHistoryByStatus(offset, pageable.getPageSize());
        int totalCount = paymentHistoryRepository.countRequestedRefund();
        return new PageImpl<>(paymentList, pageable, totalCount);
    }

    @Transactional
    public void insertCancelHistory(TossHistoryDTO historyDTO) {
        paymentHistoryRepository.insertCancelHistory(historyDTO);
    }

    @Transactional
    public void updateRefundPoint(String amountStr, int id, int userId) {
        int currentPoint = paymentHistoryRepository.findPointById(userId);
        if (currentPoint >= Integer.parseInt(amountStr)) {
            int amount = currentPoint - Integer.parseInt(amountStr);
            paymentHistoryRepository.updateUserPoint(amount, userId);
            paymentHistoryRepository.updateHistoryStatus(id);
        } else {
            System.out.println("금액이 작아서 작동이 안돼");
        }
    }
}
