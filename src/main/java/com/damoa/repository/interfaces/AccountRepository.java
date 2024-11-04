package com.damoa.repository.interfaces;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface AccountRepository {
    // 계좌 등록 신청하기
    public void addBankAccountReq(@Param("bankCode")String bankCode,
                                  @Param("accountNo")String accountNo,
                                  @Param("bankOwner")String bankOwner);
}
