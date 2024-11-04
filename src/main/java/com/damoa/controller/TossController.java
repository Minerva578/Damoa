package com.damoa.controller;

import com.damoa.dto.TossApproveResponse;
import com.damoa.dto.TossHistoryDTO;
import com.damoa.repository.model.User;
import com.damoa.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/toss")
@RequiredArgsConstructor
public class TossController {

    private final PaymentService payService;

    @PostMapping("/pay")
    public String tossPage(@RequestParam(name = "amount") int amount, Model model, @SessionAttribute(name = "principal") User principal) {
        String orderId = payService.getOrderId();
        System.out.println("payService : " + orderId);
        System.out.println("payService : " + amount);

        model.addAttribute("amount", amount);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderName", "포인트 충전");
        model.addAttribute("customerName", principal.getUsername());
        return "/tossPay";
    }

    @Transactional
    @GetMapping("/success")
    public ResponseEntity<String> successPage(@SessionAttribute(name = "principal") User principal, @RequestParam(name = "orderId") String orderId, @RequestParam(name = "paymentKey") String paymentKey, @RequestParam(name = "amount") String amount) throws IOException, InterruptedIOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic dGVzdF9za181T1dSYXBkQThkd1JPbTRtTURPbnJvMXpFcVpLOg=="); // basic64 << 인코딩
        headers.add("Content-Type", "application/json");


        // JSON 형식의 요청 본문 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);
        requestBody.put("amount", amount);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<TossApproveResponse> response = restTemplate.exchange(
                    "https://api.tosspayments.com/v1/payments/confirm", HttpMethod.POST, requestEntity, TossApproveResponse.class);

            TossApproveResponse response2 = response.getBody();

            payService.insertTossHistory(response2, principal.getId());
            payService.updatePoint(amount, principal.getId());
        } catch (HttpClientErrorException e) {
            System.err.println("Error response body: " + e.getResponseBodyAsString());
        }
        String script = "<script>window.close();</script>";
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(script);
    }


    @GetMapping("/fail")
    public String fail() {

        return "/main";
    }

    @ResponseBody
    @PostMapping("/refund")
    public String paymemtHistoryPage(@RequestBody TossHistoryDTO dto) throws IOException, InterruptedException {

        TossHistoryDTO historyDTO = payService.findPaymentHistory(dto.getId());

        String uri = "https://api.tosspayments.com/v1/payments/" + historyDTO.getPaymentKey() + "/cancel";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)) // paymentKey
                // 시크릿 키를 Basic Authorization 방식으로 base64를 이용하여 인코딩하여 꼭 보내야함
                .header("Authorization", "Basic dGVzdF9za181T1dSYXBkQThkd1JPbTRtTURPbnJvMXpFcVpLOg==")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"고객이 취소를 원함\"}")).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        payService.updateRefundPoint(historyDTO.getAmount(), historyDTO.getId(), historyDTO.getUserId());
        payService.insertCancelHistory(historyDTO);

        return response.body();
    }

    @GetMapping("/store")
    public String storePage() {
        return "/store";
    }
}
