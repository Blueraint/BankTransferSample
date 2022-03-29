package com.kakaobank.KakaoFriendTransfer.mapper;

import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalExceptionFactory;
import com.kakaobank.KakaoFriendTransfer.domain.Bank;
import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.repository.redis.RedisCustomerAccountRepository;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.CustomerAccountRepository;
import com.kakaobank.KakaoFriendTransfer.service.CustomerAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferMapper {
    private final GlobalExceptionFactory globalExceptionFactory;
    private final CustomerAccountService customerAccountService;


    @Transactional
    public Transfer dtoToNewEntity(TransferDto transferDto) throws GlobalException {
        if ( transferDto == null ) {
            return null;
        }

        CustomerAccount findSendCustomerAccount =
                customerAccountService.findByBankCodeAndAccountNumber(transferDto.getSendBankCode(), transferDto.getSendAccountNumber());
        CustomerAccount findReceiveCustomerAccount =
                customerAccountService.findByBankCodeAndAccountNumber(transferDto.getReceiveBankCode(), transferDto.getReceiveAccountNumber());

        if(findSendCustomerAccount != null && findReceiveCustomerAccount != null) {
            Transfer transfer = new Transfer(transferDto, findSendCustomerAccount, findReceiveCustomerAccount);
            transfer.setTransferAmt( transferDto.getTransferAmt() );

            return transfer;
        } else {
            // Exception 300
            throw globalExceptionFactory.globalExceptionBuilder("300");
        }
    }

    public Transfer dtoToEntity(TransferDto transferDto) throws GlobalException {
        if ( transferDto == null ) {
            return null;
        }

        Transfer transfer = new Transfer();

        transfer.setSendCustomerAccount(new CustomerAccount(new Bank(transferDto.getSendBankCode()), transferDto.getSendAccountNumber()));
        transfer.setReceiveCustomerAccount(new CustomerAccount(new Bank(transferDto.getReceiveBankCode()), transferDto.getReceiveAccountNumber()));
        transfer.setTransferAmt( transferDto.getTransferAmt() );
        transfer.setTransferStatus( transferDto.getTransferStatus() );

        return transfer;
    }

    public TransferDto entityToDto(Transfer transfer) {
        if ( transfer == null ) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        TransferDto transferDto = new TransferDto();

        transferDto.setSendBankCode( transfer.getSendCustomerAccount().getBank().getBankCode() );
        transferDto.setSendAccountNumber( transfer.getSendCustomerAccount().getAccountNumber() );

        transferDto.setReceiveBankCode( transfer.getReceiveCustomerAccount().getBank().getBankCode() );
        transferDto.setReceiveAccountNumber( transfer.getReceiveCustomerAccount().getAccountNumber() );

        transferDto.setTransferAmt( transfer.getTransferAmt() );
        transferDto.setTransferStatus( transfer.getTransferStatus() );
        transferDto.setRegDate(LocalDateTime.parse(transfer.getRegDate(), formatter) );
        transferDto.setModifyDate(LocalDateTime.parse(transfer.getModifyDate(), formatter));

        return transferDto;
    }
}

