package com.damoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/contract")
public class AdminContractController {

    @GetMapping("")
    public String contractListPage(){
        return "admin/admin_contract_list";
    }

}
