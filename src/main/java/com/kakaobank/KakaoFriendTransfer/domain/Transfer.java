package com.kakaobank.KakaoFriendTransfer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
/*
@SequenceGenerator(
        name = "TRANSFER_SEQ_GENERATOR",
        sequenceName = "TRANSFER_ID_SEQ",
        initialValue = 1,
        allocationSize = 100
)
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TRANSFER")
public class Transfer extends DateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "TRANSFER_ID")
    private Long id;

    @Column(name = "TRN_DT")
    private String trnDt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "SEND_ACCOUNT_ID",
            nullable = false
    )
    @JsonIgnore
    private CustomerAccount sendCustomerAccount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "RCV_ACCOUNT_ID",
            nullable = false
    )
    @JsonIgnore
    private CustomerAccount receiveCustomerAccount;

    @Column(name = "TRANSFER_AMT")
    private Long transferAmt;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSFER_STATUS")
    private TransferStatus transferStatus;

    // Not Valid(Account) Constructor
    /*
    public Transfer(TransferDto transferDto) {
        this.sendCustomerAccount = new CustomerAccount(new Bank(transferDto.getSendBankCode()), transferDto.getSendAccountNumber());
        this.receiveCustomerAccount = new CustomerAccount(new Bank(transferDto.getReceiveBankCode()), transferDto.getReceiveAccountNumber());
        this.transferAmt = transferDto.getTransferAmt();

        this.transferStatus = TransferStatus.WAITING;
        this.regDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }
     */

    // Valid(Account) Constructor
    public Transfer(TransferDto transferDto, CustomerAccount sendCustomerAccount, CustomerAccount receiveCustomerAccount) {
        this.sendCustomerAccount = sendCustomerAccount;
        this.receiveCustomerAccount = receiveCustomerAccount;
        this.transferAmt = transferDto.getTransferAmt();

        this.transferStatus = TransferStatus.WAITING;
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.modifyDate = regDate;
        this.trnDt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public void increaseSenderBalance() {
        Long balanceAmt = sendCustomerAccount.getBalance() + transferAmt;
        sendCustomerAccount.setBalance(balanceAmt);
    }

    public void decreaseSenderBalance() {
        Long balanceAmt = sendCustomerAccount.getBalance() - transferAmt;
        sendCustomerAccount.setBalance(balanceAmt);
    }

    public void increaseReceiverBalance() {
        Long balanceAmt = receiveCustomerAccount.getBalance() + transferAmt;
        receiveCustomerAccount.setBalance(balanceAmt);
    }

    public void decreaseReceiverBalance() {
        Long balanceAmt = receiveCustomerAccount.getBalance() - transferAmt;
        receiveCustomerAccount.setBalance(balanceAmt);
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", trnDt=" + trnDt +
//                ", sendCustomerAccountNum=" + sendCustomerAccount.getAccountNumber() +
//                ", receiveCustomerAccountNum=" + receiveCustomerAccount.getAccountNumber() +
                ", transferAmt=" + transferAmt +
                ", transferStatus=" + transferStatus +
                ", regDate=" + regDate +
                '}';
    }
}
