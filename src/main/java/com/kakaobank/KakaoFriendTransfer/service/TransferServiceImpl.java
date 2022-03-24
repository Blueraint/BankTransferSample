package com.kakaobank.KakaoFriendTransfer.service;

import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalExceptionFactory;
import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.TransferStatus;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferSearchParam;
import com.kakaobank.KakaoFriendTransfer.mapper.TransferMapper;
import com.kakaobank.KakaoFriendTransfer.repository.CustomerAccountRepository;
import com.kakaobank.KakaoFriendTransfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService{
    private final TransferRepository transferRepository;
    private final CustomerAccountRepository customerAccountRepository;
    private final GlobalExceptionFactory globalExceptionFactory;
    private final TransferMapper transferMapper;

    @Override
    public TransferDto findTransfer(Long id) {
        Optional<Transfer> transfer = transferRepository.findById(id);

        return (!transfer.isEmpty()) ? transferMapper.entityToDto(transfer.get()) : null;
    }

    @Override
    public List<TransferDto> findTransferList(TransferSearchParam searchParam) {
        return transferRepository.searchTransfer(searchParam);
    }

    @Override
    public Page<TransferDto> findTransferPage(TransferSearchParam searchParam, Pageable pageable) {
        return transferRepository.searchTransferPage(searchParam, pageable);
    }

    @Override
    public List<TransferDto> findTransferListBySender(String sendKakaoUserId) {
        return transferRepository.findBySendCustomerAccountKakaoFriendUserId(sendKakaoUserId)
                .stream().map(transferMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransferDto> findTransferListByReceiver(String receiveKakaoUserId) {
        return transferRepository.findByReceiveCustomerAccountKakaoFriendUserId(receiveKakaoUserId)
                .stream().map(transferMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public boolean transferAccountValidation(Transfer transfer) {
        // 인증되지 않은 문제 있는 계좌인 경우
        if(!transfer.getSendCustomerAccount().isCertified() || !transfer.getReceiveCustomerAccount().isCertified())
            return false;
        else return true;
    }

    @Override
    @Transactional
    public void saveTransfer(Transfer transfer) throws GlobalException {
        if(!transferAccountValidation(transfer))
//            throw new IllegalStateException("Invalid Sender/Receiver Account.");
            throw globalExceptionFactory.globalExceptionBuilder("100");

        // 계좌 잔액이 전송금액보다 작은 경우
        if(transfer.getSendCustomerAccount().getBalance() < transfer.getTransferAmt())
//            throw new IllegalArgumentException("TransferAmt must lower than balance in sender's account.");
            throw globalExceptionFactory.globalExceptionBuilder("200");

        // 송신자 계좌 잔액 감액
        transfer.decreaseSenderBalance();

        // 수신자 계좌 잔액 증액은 이체를 승인한 시점에 수행

        log.debug("Create New Transfer : {}", transfer.toString());
        // 이체내역 저장
        transferRepository.save(transfer);
        // 계좌변경사항 저장
        customerAccountRepository.save(transfer.getSendCustomerAccount());
    }

    @Override
    @Transactional
    public void cancelTransfer(Transfer transfer) {
        // 감액되었던 송신자 계좌 잔액 복구
        transfer.increaseSenderBalance();

        // 이체내역 상태값 변경
        transfer.setTransferStatus(TransferStatus.CANCEL);

        log.debug("Cancel Current Transfer : {}", transfer.toString());
        // 이체내역 update
        transferRepository.save(transfer);
        // 계좌변경사항 저장
        customerAccountRepository.save(transfer.getSendCustomerAccount());
    }

    @Override
    @Transactional
    public void confirmTransfer(Transfer transfer) {
        // 승인에 따른 수신자 계좌 잔액 증액
        transfer.increaseReceiverBalance();

        // 이체내역 상태값 변경
        transfer.setTransferStatus(TransferStatus.SUCCESS);

        log.debug("Confirm Transfer : {}", transfer.toString());
        // 이체내역 update
        transferRepository.save(transfer);
        // 계좌변경사항 저장
        customerAccountRepository.save(transfer.getReceiveCustomerAccount());
    }
}
