package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;

import java.util.List;

public interface TransferService {
    Transfer findTransfer(Long id);
    TransferDto findTransferDto(Long id);

    List<Transfer> findTransferListBySender(String sendKakaoUserId);
    List<Transfer> findTransferListByReceiver(String receiveKakaoUserId);

    boolean transferAccountValidation(Transfer transfer);

    void saveTransfer(Transfer transfer) throws GlobalException;

    Transfer cancelTransfer(Long id);

    Transfer confirmTransfer(Long id);

    Transfer save(Long id, Transfer transfer);
}
