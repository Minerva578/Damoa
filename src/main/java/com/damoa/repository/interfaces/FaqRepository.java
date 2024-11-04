package com.damoa.repository.interfaces;

import com.damoa.dto.admin.FaqUpdateDTO;
import com.damoa.repository.model.Faq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface FaqRepository {

    public List<Faq> getAllQna();
    public Faq findById(int id);
    public int updateById(FaqUpdateDTO updateDTO);
    public int insert(Faq faq);
    public int delete(int id);
}
