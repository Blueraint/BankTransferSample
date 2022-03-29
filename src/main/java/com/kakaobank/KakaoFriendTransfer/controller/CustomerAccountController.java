package com.kakaobank.KakaoFriendTransfer.controller;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.dto.CustomerAccountDto;
import com.kakaobank.KakaoFriendTransfer.mapper.CustomerAccountMapper;
import com.kakaobank.KakaoFriendTransfer.service.CustomerAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class CustomerAccountController {
    private final CustomerAccountService customerAccountService;

    @GetMapping
    public CustomerAccountDto findCustomerAccount(String bankCode, String accountNumber) {
        return CustomerAccountMapper.INSTANCE.entityToDto(customerAccountService.findByBankCodeAndAccountNumber(bankCode, accountNumber));
    }

    @GetMapping("/kakao")
    public CustomerAccountDto findCustomerAccountByKakaoUserId(String kakaoUserId) {
        return customerAccountService.findByKakaoUserId(kakaoUserId);
    }
}
