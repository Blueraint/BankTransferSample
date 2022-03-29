package com.kakaobank.KakaoFriendTransfer;

import com.kakaobank.KakaoFriendTransfer.domain.*;
import com.kakaobank.KakaoFriendTransfer.domain.dto.TransferDto;
import com.kakaobank.KakaoFriendTransfer.mapper.TransferMapper;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class RepositoryTest {
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
    private TransferMapper transferMapper;

    @BeforeEach
    public void setData() {
        /*
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
        */

        // Transfer
        TransferDto transferDto = new TransferDto();
        transferDto.setSendBankCode("003"); transferDto.setSendAccountNumber("1234ABC");
        transferDto.setReceiveBankCode("003"); transferDto.setReceiveAccountNumber("9999TTTT");
        transferDto.setTransferAmt(10000L);

        Transfer transfer = transferMapper.dtoToNewEntity(transferDto);
        transferRepository.saveAndFlush(transfer);
    }


    @Test
    public void bankRepositoryTest() {

        Bank findBank = bankRepository.findById(1L).get();
        assertThat(findBank.getBankCode()).isEqualTo("003");
        assertThat(findBank.getBankName()).isEqualTo("IBK");
    }

    @Test
    public void customerRepositoryTest() {

        Customer findCustomer = customerRepository.findById(1L).get();
        assertThat(findCustomer.getCi()).isEqualTo("1234561234567");
        assertThat(findCustomer.getName()).isEqualTo("Kim");
    }

    @Test
    public void customerAccountRepositoryTest() {
        CustomerAccount findCustomerAccount = customerAccountRepository.findByBankBankCodeAndAccountNumber("003","1234ABC").get();
        assertThat(findCustomerAccount.getAccountNumber()).isEqualTo("1234ABC");
        assertThat(findCustomerAccount.getCustomer().getName()).isEqualTo("Kim");
        assertThat(findCustomerAccount.getKakaoFriend().getUserId()).isEqualTo("sampleId1");
        assertThat(findCustomerAccount.getBank().getBankCode()).isEqualTo("003");

        Customer customer = customerRepository.findByCi("1234561234567").get();
        List<CustomerAccount> customerAccountList = customerAccountRepository.findByCustomer(customer);
        customerAccountList.forEach(customerAccount -> System.out.println("###CustomerAccount : " + customerAccount.toString()));
        assertThat(customerAccountList.size()).isEqualTo(2);
    }

    @Test
    public void transferRepositoryTest() {
        CustomerAccount customerAccount = customerAccountRepository.findByBankBankCodeAndAccountNumber("003","9999TTTT").get();
        List<Transfer> transferList = transferRepository.findByReceiveCustomerAccount(customerAccount);
        transferList.forEach(transfer -> System.out.println("###Transfer : " + transfer.toString()));
    }

    @Test
    @Transactional
    public void bulkUpdateTest() {
        IntStream.rangeClosed(1,10).forEach(i -> {
            System.out.println("### Thread Sleep(sec " + i + ")");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        String timeoutCriteriaDate = LocalDateTime.now().minusSeconds(5).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        System.out.println("@@@ timeout : " + timeoutCriteriaDate);

        int count = transferRepository.bulkTransferCancel(timeoutCriteriaDate);
        System.out.println("@@@ update count : " + count);
        transferRepository.findAll().forEach(transfer -> System.out.println("### Transfer : " + transfer.toString()));
        assertThat(transferRepository.findAll().get(0).getTransferStatus()).isEqualTo(TransferStatus.CANCEL);
    }
}
