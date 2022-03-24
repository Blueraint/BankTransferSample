package com.kakaobank.KakaoFriendTransfer.mapper;

import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalExceptionFactory;
import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.Transfer;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.repository.CustomerAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransferMapper {
    private final CustomerAccountRepository customerAccountRepository;
    private final GlobalExceptionFactory globalExceptionFactory;

    public Transfer dtoToEntity(TransferDto transferDto) throws GlobalException {
        if ( transferDto == null ) {
            return null;
        }

        Transfer transfer = new Transfer();

        Optional<CustomerAccount> findSendCustomerAccount =
                customerAccountRepository.findByBankBankCodeAndAccountNumber(transferDto.getSendBankCode(), transferDto.getSendAccountNumber());
        Optional<CustomerAccount> findReceiveCustomerAccount =
                customerAccountRepository.findByBankBankCodeAndAccountNumber(transferDto.getReceiveBankCode(), transferDto.getReceiveAccountNumber());

        if(!findSendCustomerAccount.isEmpty()) {
            transfer.setSendCustomerAccount(findSendCustomerAccount.get());
            transfer.setReceiveCustomerAccount(findReceiveCustomerAccount.get());
        } else {
            // Exception 300
            throw globalExceptionFactory.globalExceptionBuilder("300");
        }
        transfer.setTransferAmt( transferDto.getTransferAmt() );
        transfer.setTransferStatus( transferDto.getTransferStatus() );

        return transfer;
    }

    public TransferDto entityToDto(Transfer transfer) {
        if ( transfer == null ) {
            return null;
        }

        TransferDto transferDto = new TransferDto();

        transferDto.setSendBankCode( transfer.getSendCustomerAccount().getBank().getBankCode() );
        transferDto.setSendAccountNumber( transfer.getSendCustomerAccount().getAccountNumber() );

        transferDto.setReceiveBankCode( transfer.getReceiveCustomerAccount().getBank().getBankCode() );
        transferDto.setReceiveAccountNumber( transfer.getReceiveCustomerAccount().getAccountNumber() );

        transferDto.setTransferAmt( transfer.getTransferAmt() );
        transferDto.setTransferStatus( transfer.getTransferStatus() );
        transferDto.setRegDate( transfer.getRegDate() );

        return transferDto;
    }
}

