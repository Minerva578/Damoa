package com.damoa.controller;

import com.damoa.repository.model.Sign;
import com.damoa.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private SignCotroller signCotroller;

    @Autowired
    private SignService signService;

    @GetMapping("/list")
    public String contractListPage(){
        return "contract/list";
    }

    @GetMapping("/test")
    public String test(Model model){
        int userId=1; // 세션값으로 수정 예정
        
        // signList 전달
        List<Sign> signList = signService.findSignById(userId);
        signList = signCotroller.setSignPath(signList);
        model.addAttribute("signList",signList);

        return "contract/test_form";
    }
}
