package com.damoa.controller;

import com.damoa.dto.AddSignDTO;
import com.damoa.repository.model.Sign;
import com.damoa.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignCotroller {

    private final SignService signService;

    @Value("${file.upload-dir-sign}")
    private String uploadSigndir;

    /**
     * 전자 서명 등록 페이지
     *
     * @return
     */
    @GetMapping("")
    public String signRegisterPage() {
        return "sign/make_sign";
    }

    /**
     * 전자 서명 등록 요청
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public String saveRegisterProc(@RequestBody Map<String, String> request) {
        String base64Image = request.get("image"); // 이미지 바이너리 데이터 받기
        String name = request.get("name"); // 사인 이름 받기

        // 메타데이터 제거
        String imageData = base64Image.split(",")[1];
        // 비정상적인 문자 제거
        imageData = imageData.replaceAll("[^A-Za-z0-9+/=]", "");

        // 패딩 추가
        while (imageData.length() % 4 != 0) {
            imageData += "=";
        }

        // Base64 문자열을 byte[]로 변환
        byte[] imageBytes = Base64.getDecoder().decode(imageData);
        // 파일 저장 경로
        String filePath = uploadSigndir; // 저장할 경로
        int userId = 1; // 세션값 불러올 예정

        // 사인 파일명
        String uploadFileName = userId + "_" + UUID.randomUUID() + ".png";

        AddSignDTO newSign = AddSignDTO.builder()
                .name(name)
                .fileData(imageBytes)
                .userId(userId)
                .uploadFileName(uploadFileName)
                .build();

        // 이미지 파일로 저장
        try (FileOutputStream fos = new FileOutputStream(filePath + File.separator + uploadFileName)) {
            fos.write(imageBytes);
            signService.addNewSign(newSign);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 저장 실패").toString();
        }

        return ResponseEntity.ok("이미지 저장 성공").toString();
    }

    @GetMapping("/list")
    private String signListPage(Model model) {
        int userId = 1; // 세션값으로 수정 예정
        List<Sign> signList = signService.findSignById(userId);
        signList = setSignPath(signList);
        model.addAttribute("signList", signList);
        return "sign/sign_list";
    }

    // 사인 경로 설정
    public List<Sign> setSignPath(List<Sign> list) {
        for (int a = 0; a < list.size(); a++) {
            list.get(a).setFileName(list.get(a).setUpSignImage());
        }
        return list;
    }
}
