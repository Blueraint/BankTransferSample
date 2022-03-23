package com.kakaobank.KakaoFriendTransfer.repository;

import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferSearchParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class TransferRepositoryImpl implements  TransferRepositoryCustom{

    // Implementation by querydsl or mybatis
    @Override
    public List<TransferDto> searchTransfer(TransferSearchParam searchParam) {
        return null;
    }

    @Override
    public Page<TransferDto> searchTransferPage(TransferSearchParam searchParam, Pageable pageable) {
        return null;
    }
}
