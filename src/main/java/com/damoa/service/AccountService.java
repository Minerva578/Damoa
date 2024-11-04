package com.damoa.service;

import com.damoa.dto.BankAuthDTO;
import com.damoa.repository.interfaces.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // 계좌 등록 신청하기
    public void addAccountReq(BankAuthDTO reqDto){
        System.out.println(reqDto+"서비스 들어옴");
        accountRepository.addBankAccountReq(reqDto.bankCodeStd,
                reqDto.accountNum, reqDto.bankOwner );
    }

}
