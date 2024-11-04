package com.damoa.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TossApproveResponse {
    String mid;
    String version;
    String paymentKey; // 승인 키값 있어야 환불가능
    String orderId; // 주문 번호
    String orderName; // 주문상품  티켓 or CON
    String currency;
    String method;
    String totalAmount; // 토탈금액
    String balanceAmount; // 세전금액
    String suppliedAmount; // 세금
    String vat;
    String status;
    String requestedAt; // 승인요청 시간
    String approvedAt; // 승인한 시간
    String useEscrow;
    String cultureExpense;
    PaymentSuccessCardDTO card;
    String type;
}
