package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferSearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransferService {
    List<TransferDto> findTransferList(TransferSearchParam searchParam);
    Page<TransferDto> findTransferPage(TransferSearchParam searchParam, Pageable pageable);

    boolean transferAccountValidation(Transfer transfer);

    void saveTransfer(Transfer transfer) throws GlobalException;

    void cancelTransfer(Transfer transfer);

    void confirmTransfer(Transfer transfer);
}
