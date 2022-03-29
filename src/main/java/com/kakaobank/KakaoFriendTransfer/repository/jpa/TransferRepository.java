package com.kakaobank.KakaoFriendTransfer.repository.jpa;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("jpaTransferRepository")
public interface TransferRepository extends JpaRepository<Transfer, Long>  {
    List<Transfer> findBySendCustomerAccount(CustomerAccount sendCustomerAccount);
    List<Transfer> findByReceiveCustomerAccount(CustomerAccount receiveCustomerAccount);
    List<Transfer> findByTransferStatus(TransferStatus transferStatus);
    List<Transfer> findBySendCustomerAccountKakaoFriendUserId(String userId);
    List<Transfer> findByReceiveCustomerAccountKakaoFriendUserId(String userId);

    /*
    * 동시성 제어를 위한 고려 필요 (이체내역 관련 Service 에서 격리 수준 고려)
    */
//  void transferRepository.save(Transfer transfer);

    /* 동시성 제어를 위한 고려 필요 (Database 수준 격리 실행할 것인지?) */
    @Modifying(clearAutomatically = true)
    @Query("update Transfer t set t.transferStatus = 'CANCEL' where t.transferStatus = 'WAITING' and t.regDate < :regDate")
    int bulkTransferCancel(@Param("regDate") String regDate);
}
