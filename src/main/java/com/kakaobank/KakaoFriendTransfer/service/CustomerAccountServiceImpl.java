package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.dto.CustomerAccountDto;
import com.kakaobank.KakaoFriendTransfer.mapper.CustomerAccountMapper;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.CustomerAccountRepository;
import com.kakaobank.KakaoFriendTransfer.repository.redis.RedisCustomerAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerAccountServiceImpl implements CustomerAccountService {
    private final CustomerAccountRepository customerAccountRepository;

    @Override
    public CustomerAccount findByBankCodeAndAccountNumber(String bankCode, String accountNumber) {
        /* We Don't use find account information service to redis
         * Why? -> Account Service Common Service! (Outer API) 데이터베이스 불일치가 발생할 수 있다!(Redis information != DB information)
         * DataBase 에서 조회하는 것으로 한다
         */

        // Find to Database
        Optional<CustomerAccount> optionalCustomerAccount =  customerAccountRepository.findByBankBankCodeAndAccountNumber(bankCode, accountNumber);

        if(optionalCustomerAccount.isEmpty()) return null;

        // If Exists in Database
        CustomerAccount customerAccount = optionalCustomerAccount.get();

        return customerAccount;
    }

    @Override
    public CustomerAccountDto findByKakaoUserId(String kakaoUserId) {
        Optional<CustomerAccount> optionalCustomerAccount = customerAccountRepository.findByKakaoFriendUserId(kakaoUserId);

        if(optionalCustomerAccount.isEmpty()) return null;

        CustomerAccount customerAccount = optionalCustomerAccount.get();

        return CustomerAccountMapper.INSTANCE.entityToDto(customerAccount);
    }
}
