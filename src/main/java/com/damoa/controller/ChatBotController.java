package com.damoa.controller;

import com.damoa.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatBotController {

    private static String secretKey = "bGptUXprVG9MbmJqSm1CUmRET1NGa2lVbkZQaVZtYno=";
    private static String apiUrl = "https://w5c8g8mwvx.apigw.ntruss.com/custom/v1/16045/fb1a2cd144e7b2fa34c424396081a3fa5a53d7eeb8838c69e4d4a32e476ee2be";

    //    /**
//     * 보낼 메세지를 네이버에서 제공해준 암호화로 변경해주는 메소드
//     * 시크릿 키를 Base64로 인코딩하는 메서드
//     * @param secretKey
//     * @return encodeBase64String
//     */
    public static String makeSignature(String message, String secretKey) {
        // 시크릿 키를 바이트 배열로 변환
        String encodeBase64String = "";
        try {
            byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);

            byte[] signature = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            encodeBase64String = Base64.getEncoder().encodeToString(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeBase64String;
    }

    @GetMapping("/chat")
    public String chatPage() {

        return "chatbot";
    }

    @ResponseBody
    @PostMapping("/fetchMessage")
    public Map<String, String> sendMessage(@RequestBody MessageDTO req) {
        String chatbotMessage = "";
        String description = "";
        String voiceMessage = req.getMessage();
        try {
            //String apiURL = "https://ex9av8bv0e.apigw.ntruss.com/custom_chatbot/prod/";

            URL url = new URL(apiUrl);

            String message = getReqMessage(voiceMessage);
            System.out.println("##" + message);

            String encodeBase64String = makeSignature(message, secretKey);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;UTF-8");
            con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

            // post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(message.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();

            BufferedReader br;

            if (responseCode == 200) { // Normal call
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                con.getInputStream()));
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    chatbotMessage = decodedString;
                    System.out.println(chatbotMessage);
                }
                //받아온 값을 세팅하는 부분
                String jsonString = chatbotMessage;

                // JSON 문자열을 JSONObject로 변환
                JSONObject jsonObject = new JSONObject(jsonString);

                // bubbles 배열에서 첫 번째 요소 가져오기
                JSONArray bubbles = jsonObject.getJSONArray("bubbles");
                JSONObject firstBubble = bubbles.getJSONObject(0);

                // description 값 추출
                description = firstBubble.getJSONObject("data").getString("description");
                System.out.println(description);
                //chatbotMessage = decodedString;
                in.close();

            } else {  // Error occurred
                chatbotMessage = con.getResponseMessage();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        // JSON 형태로 응답을 보냅니다.
        Map<String, String> response1 = new HashMap<>();
        response1.put("description", description);
        return response1;
    }

    public static String getReqMessage(String voiceMessage) {

        String requestBody = "";
        try {

            JSONObject body = new JSONObject();
            long timestamp = new Date().getTime();

            body.put("version", "v2");
            //body.put("userId", UUID.randomUUID().toString());
            body.put("userId", "U47b00b58c90f8e47428af8b7bddc1231heo2");
            body.put("timestamp", timestamp);

            JSONObject bubblesObject = new JSONObject();
            bubblesObject.put("type", "text");

            JSONObject dataObject = new JSONObject();
            String voiceMessageSafe = (voiceMessage != null) ? voiceMessage : "";
            dataObject.put("description", voiceMessageSafe);

            bubblesObject.put("data", dataObject);

            JSONArray bubblesArray = new JSONArray();
            bubblesArray.put(bubblesObject);

            body.put("bubbles", bubblesArray);
            body.put("event", "send");
            requestBody = body.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while communicating with the chatbot service.";
        }
        return requestBody;
    }
}
