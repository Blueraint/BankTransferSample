package com.kakaobank.KakaoFriendTransfer;

import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalException;
import com.kakaobank.KakaoFriendTransfer.config.exception.GlobalExceptionFactory;
import com.kakaobank.KakaoFriendTransfer.domain.*;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.repository.*;
import com.kakaobank.KakaoFriendTransfer.service.KakaoFriendService;
import com.kakaobank.KakaoFriendTransfer.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ErrCodeRepository errCodeRepository;
    @Autowired
    private KakaoFriendRepository kakaoFriendRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private KakaoFriendService kakaoFriendService;
    @Autowired
    private TransferService transferService;
    @Autowired
    GlobalExceptionFactory globalExceptionFactory;

    @BeforeEach
    public void setData() {
        // Bank
        List<Bank> bankList = new ArrayList<>();
        Bank bank = new Bank();
        bank.setBankCode("003");
        bank.setBankName("IBK");

        Bank bank2 = new Bank();
        bank2.setBankCode("020");
        bank2.setBankName("WOORI");

        Bank bank3 = new Bank();
        bank3.setBankCode("026");
        bank3.setBankName("SHINHAN");

        bankList.add(bank); bankList.add(bank2); bankList.add(bank3);
        bankRepository.saveAllAndFlush(bankList);


        //Customer
        List<Customer> customerList = new ArrayList<>();
        Customer customer = new Customer();
        customer.setName("Kim");
        customer.setAddress("Seoul, Korea");
        customer.setCi("1234561234567");

        Customer customer2 = new Customer();
        customer2.setName("Park");
        customer2.setAddress("Pusan, Korea");
        customer2.setCi("9232291239122");

        Customer customer3 = new Customer();
        customer3.setName("Patrick");
        customer3.setAddress("NewYork, US");
        customer3.setCi("0889013929222");

        customerList.add(customer); customerList.add(customer2); customerList.add(customer3);
        customerRepository.saveAllAndFlush(customerList);


        // KakaoFriend
        List<KakaoFriend> kakaoFriendList = new ArrayList<>();

        KakaoFriend kakaoFriend = new KakaoFriend();
        kakaoFriend.setUserId("sampleId1");
        KakaoFriend kakaoFriend2 = new KakaoFriend();
        kakaoFriend2.setUserId("sampleId2");
        KakaoFriend kakaoFriend3 = new KakaoFriend();
        kakaoFriend3.setUserId("sampleId3");

        kakaoFriendList.add(kakaoFriend); kakaoFriendList.add(kakaoFriend2); kakaoFriendList.add(kakaoFriend3);
        kakaoFriendRepository.saveAllAndFlush(kakaoFriendList);

        // CustomerAccount
        Bank findBank = bankRepository.findByBankCode("003").get();

        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setAccountNumber("1234ABC");
        customerAccount.setBalance(100000L);
        customerAccount.setCustomer(customer);
        customerAccount.setKakaoFriend(kakaoFriend);
        customerAccount.setBank(findBank);
        customerAccount.setCertified(true);

        CustomerAccount customerAccount2 = new CustomerAccount();
        customerAccount2.setAccountNumber("9999TTTT");
        customerAccount2.setBalance(50000L);
        customerAccount2.setCustomer(customer);
        customerAccount2.setBank(findBank);
        customerAccount2.setCertified(true);

        customerAccountRepository.saveAndFlush(customerAccount);
        customerAccountRepository.saveAndFlush(customerAccount2);


        // Transfer
        /*
        TransferDto transferDto = new TransferDto();
        transferDto.setSendCustomerAccount(customerAccount);
        transferDto.setReceiveCustomerAccount(customerAccount2);
        transferDto.setTransferAmt(10000L);

        Transfer transfer = new Transfer(transferDto);
        transferRepository.saveAndFlush(transfer);

         */
    }

    @BeforeEach
    public void setErrCodeMap() {
        ErrCode errCode100 = new ErrCode("100","Invalid Sender/Receiver Account.");
        ErrCode errCode200 = new ErrCode("200","TransferAmt must lower than balance in sender's account.");
        ErrCode errCode300 = new ErrCode("300","Sender/Receiver Account must not null.");

        errCodeRepository.save(errCode100);
        errCodeRepository.save(errCode200);
        errCodeRepository.save(errCode300);
    }

    @Test
    public void kakaoFriendServiceTest() {

    }

    @Test
    public void transferExceptionTest() {
        CustomerAccount customerAccount = customerAccountRepository.findByAccountNumber("1234ABC").get();
        CustomerAccount customerAccount2 = customerAccountRepository.findByAccountNumber("9999TTTT").get();

        TransferDto transferDto = new TransferDto();
        transferDto.setSendCustomerAccount(customerAccount);
        transferDto.setReceiveCustomerAccount(customerAccount2);
        transferDto.setTransferAmt(200000L);

        Transfer transfer = new Transfer(transferDto);

        try {
            transferService.saveTransfer(transfer);
        }catch (GlobalException e) {
            assertThat(e.getErrCode()).isEqualTo("200");
        }
    }

    @Test
    public void transferCancelServiceTest() throws GlobalException {
        CustomerAccount customerAccount = customerAccountRepository.findByAccountNumber("1234ABC").get();
        CustomerAccount customerAccount2 = customerAccountRepository.findByAccountNumber("9999TTTT").get();

        TransferDto transferDto = new TransferDto();
        transferDto.setSendCustomerAccount(customerAccount);
        transferDto.setReceiveCustomerAccount(customerAccount2);
        transferDto.setTransferAmt(10000L);

        Transfer transfer = new Transfer(transferDto);

        transferService.saveTransfer(transfer);

        transferRepository.flush();

        assertThat(customerAccountRepository.findByAccountNumber("1234ABC").get().getBalance()).isEqualTo(90000L);
        assertThat(customerAccountRepository.findByAccountNumber("9999TTTT").get().getBalance()).isEqualTo(50000L);

        transferService.cancelTransfer(transfer);

        transferRepository.flush();

        Transfer findTransfer = transferRepository.findBySendCustomerAccount(customerAccount).get(0);
        CustomerAccount findCustomerAccount = customerAccountRepository.findByAccountNumber("1234ABC").get();
        CustomerAccount findCustomerAccount2 = customerAccountRepository.findByAccountNumber("9999TTTT").get();

        assertThat(findTransfer.getTransferStatus()).isEqualTo(TransferStatus.CANCEL);
        assertThat(findCustomerAccount.getBalance()).isEqualTo(100000L);
        assertThat(findCustomerAccount2.getBalance()).isEqualTo(50000L);
    }

    @Test
    public void transferConfirmServiceTest() throws GlobalException {
        CustomerAccount customerAccount = customerAccountRepository.findByAccountNumber("1234ABC").get();
        CustomerAccount customerAccount2 = customerAccountRepository.findByAccountNumber("9999TTTT").get();

        TransferDto transferDto = new TransferDto();
        transferDto.setSendCustomerAccount(customerAccount);
        transferDto.setReceiveCustomerAccount(customerAccount2);
        transferDto.setTransferAmt(10000L);

        Transfer transfer = new Transfer(transferDto);

        transferService.saveTransfer(transfer);

        transferRepository.flush();

        assertThat(customerAccountRepository.findByAccountNumber("1234ABC").get().getBalance()).isEqualTo(90000L);
        assertThat(customerAccountRepository.findByAccountNumber("9999TTTT").get().getBalance()).isEqualTo(50000L);

        transferService.confirmTransfer(transfer);

        transferRepository.flush();

        Transfer findTransfer = transferRepository.findBySendCustomerAccount(customerAccount).get(0);
        CustomerAccount findCustomerAccount = customerAccountRepository.findByAccountNumber("1234ABC").get();
        CustomerAccount findCustomerAccount2 = customerAccountRepository.findByAccountNumber("9999TTTT").get();

        assertThat(findTransfer.getTransferStatus()).isEqualTo(TransferStatus.SUCCESS);
        assertThat(findCustomerAccount.getBalance()).isEqualTo(90000L);
        assertThat(findCustomerAccount2.getBalance()).isEqualTo(60000L);
    }
}
