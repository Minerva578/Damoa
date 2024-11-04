package com.damoa.service;

import com.damoa.dto.admin.FaqSaveDTO;
import com.damoa.dto.admin.FaqUpdateDTO;
import com.damoa.repository.interfaces.FaqRepository;
import com.damoa.repository.model.Faq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqService {

    @Autowired
    public FaqRepository faqRepository;

    public List<Faq> getAllQna() {
        return faqRepository.getAllQna();
    }

    public Faq getFaqById(int id) {
        return faqRepository.findById(id);
    }

    public int updateById(FaqUpdateDTO updateDTO) {
        return faqRepository.updateById(updateDTO);
    }

    public int createFaq(FaqSaveDTO dto) {
        return faqRepository.insert(dto.toFaq());
    }

    public int delete(int id) {
        return faqRepository.delete(id);
    }


}
