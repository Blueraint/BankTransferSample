package com.kakaobank.KakaoFriendTransfer.repository;

import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferSearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransferRepositoryCustom {
    List<TransferDto> searchTransfer(TransferSearchParam searchParam);
    Page<TransferDto> searchTransferPage(TransferSearchParam searchParam, Pageable pageable);
}
