package com.kakaobank.KakaoFriendTransfer.repository;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long>, TransferRepositoryCustom  {
    List<Transfer> findBySendCustomerAccount(CustomerAccount sendCustomerAccount);
    List<Transfer> findByReceiveCustomerAccount(CustomerAccount receiveCustomerAccount);
    List<Transfer> findByTransferStatus(TransferStatus transferStatus);
}
