package com.damoa.service;

import com.damoa.dto.AddSignDTO;
import com.damoa.repository.interfaces.SignRepository;
import com.damoa.repository.model.Sign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignService {

    private final SignRepository signRepository;

    /**
     * 전자 서명 등록하기
     * @param newSignDto
     */
    public void addNewSign(AddSignDTO newSignDto){
        signRepository.insertSign(newSignDto);
        return;
    }

    /**
     * 유저 id로 전자 서명 가져오기
     * @param id
     * @return
     */
    public List<Sign> findSignById(int id){
        return signRepository.selectSignsById(id);
    }
}
