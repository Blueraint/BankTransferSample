package com.kakaobank.KakaoFriendTransfer.controller;

import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferRestController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferDto> createTransfer(@RequestBody TransferDto transferDto) {
//        transferService.saveTransfer();

        return null;
    }
}
