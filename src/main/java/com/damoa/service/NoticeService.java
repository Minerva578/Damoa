package com.damoa.service;

import com.damoa.repository.interfaces.NoticeRepository;
import com.damoa.repository.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    public NoticeRepository noticeRepository;

    // 공지 목록 가져오기
    public List<Notice> getNoticeList(int limit, int offset) {
        return noticeRepository.getNoticeList(limit, offset);
    }

    // 공지 다 가져오기
    public Page<Notice> getAllNotice(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<Notice> list = noticeRepository.findNoticeList(offset, pageable.getPageSize());
        int totalCount = noticeRepository.getNoticeCount();
        return new PageImpl<>(list, pageable, totalCount);
    }

    // 공지 세부사항 가져오기
    public Notice getNotice(int id) {

        return noticeRepository.getNotice(id);
    }

    public List<Notice> findNoticeList(int page, int size) {
        int offset = (page - 1) * size;
        return noticeRepository.findNoticeList(offset, size);
    }

    public int getNoticeCount() {
        return noticeRepository.getNoticeCount();
    }

}
