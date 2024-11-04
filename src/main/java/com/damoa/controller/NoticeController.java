package com.damoa.controller;

import com.damoa.repository.model.Notice;
import com.damoa.repository.model.User;
import com.damoa.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class NoticeController {

    @Autowired
    public HttpSession session;

    @Autowired
    public NoticeService noticeService;

    @GetMapping("/notice")
    public String noticeListPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        User user = (User) session.getAttribute("principal");
        // 모든 공지 가져오기
        Page<Notice> noticePage = noticeService.getAllNotice(pageable);
        List<Notice> noticeList = noticePage.getContent();

        int currentPage = noticePage.getNumber();
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", noticePage.getTotalPages());
        model.addAttribute("nextPage", currentPage + 1 < noticePage.getTotalPages() ? currentPage + 1 : null);
        model.addAttribute("prevPage", currentPage > 0 ? currentPage - 1 : null); // 이전 페이지 번호
        model.addAttribute("pagination", noticePage);
        model.addAttribute("noticeList", noticeList);

        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        model.addAttribute("isLogin", user);
        return "/user/user_notice_list";
    }

    @GetMapping("/notice/detail/{id}")
    public String noticeDetailPage(@PathVariable("id") int id, Model model) {
        User user = (User) session.getAttribute("principal");
        Notice notice = noticeService.getNotice(id);

        model.addAttribute("notice", notice);
        if (user != null) {
            model.addAttribute("isFreelancer", user.getUserType().equals("freelancer"));
            model.addAttribute("isCompany", user.getUserType().equals("company"));
        }
        model.addAttribute("isLogin", user);
        System.out.println(notice);
        return "/user/user_notice_detail";
    }

}
