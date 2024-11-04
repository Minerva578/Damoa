package com.damoa.repository.interfaces;

import com.damoa.dto.AddSignDTO;
import com.damoa.repository.model.Sign;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SignRepository {
    public void insertSign(AddSignDTO addSignDTO);

    public List<Sign> selectSignsById(int id);
}
