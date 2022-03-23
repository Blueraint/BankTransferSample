package com.kakaobank.KakaoFriendTransfer.domain;

import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "TRANSFER")
public class Transfer extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "TRANSFER_ID")
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "SEND_ACCOUNT_ID",
            nullable = false
    )
    private CustomerAccount sendCustomerAccount;

    @OneToOne
    @JoinColumn(
            name = "RCV_ACCOUNT_ID",
            nullable = false
    )
    private CustomerAccount receiveCustomerAccount;

    @Column(name = "TRANSFER_AMT")
    private Long transferAmt;

    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    public Transfer(TransferDto transferDto) {
        this.sendCustomerAccount = transferDto.getSendCustomerAccount();
        this.receiveCustomerAccount = transferDto.getReceiveCustomerAccount();
        this.transferAmt = transferDto.getTransferAmt();

        this.transferStatus = TransferStatus.WAITING;
        this.regDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
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
                ", sendCustomerAccountNum=" + sendCustomerAccount.getAccountNumber() +
                ", receiveCustomerAccountNum=" + receiveCustomerAccount.getAccountNumber() +
                ", transferAmt=" + transferAmt +
                ", transferStatus=" + transferStatus +
                '}';
    }
}
