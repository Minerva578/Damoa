package com.damoa.controller;

import com.damoa.dto.admin.FaqSaveDTO;
import com.damoa.dto.admin.FaqUpdateDTO;
import com.damoa.repository.model.Faq;
import com.damoa.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    public FaqService faqService;


    @GetMapping("/list")
    public String qnaListPage(Model model) {
        List<Faq> faqList = faqService.getAllQna();
        model.addAttribute("faqList", faqList);
        return "admin/admin_faq_list";

    }

    @GetMapping("/detail/{id}")
    public String detailPage(@PathVariable(name ="id") int id, Model model) {
        Faq faq = faqService.getFaqById(id);
        model.addAttribute("faq", faq);
        return "admin/admin_faq_detail";
    }

    @GetMapping("/update/{id}")
    public String updatePage(@PathVariable("id") int id, Model model) {
        Faq faq = faqService.getFaqById(id);
        System.out.println(faq);
        model.addAttribute("faq", faq);
        return "admin/admin_faq_update";
    }

    @PostMapping("/update/{id}")
    public String updatePage(@PathVariable int id, @RequestParam String title, @RequestParam String content) {
        FaqUpdateDTO updateDTO = new FaqUpdateDTO(id, title, content);
        faqService.updateById(updateDTO);
        return "redirect:/faq/list";
    }
    @GetMapping("/save")
    public String savePage(){

        return "admin/faq_save_form";

    }

    @PostMapping("/save")
    public String saveProc(@ModelAttribute("reqDTO") FaqSaveDTO reqDTO){
        faqService.createFaq(reqDTO);
        return "redirect:/faq/list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        faqService.delete(id);
        return "redirect:/faq/list";
    }


}
