package com.kakaobank.KakaoFriendTransfer;

import com.kakaobank.KakaoFriendTransfer.domain.*;
import com.kakaobank.KakaoFriendTransfer.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Profile("database_dev")
@Component
@RequiredArgsConstructor
public class LocalInitialization {
    private final InitMemberService initMemberService;


    @PostConstruct
    public void init() {
        initMemberService.initializationData();
        initMemberService.initializationErrCode();
        initMemberService.initializationCache();
    }

    @Component
    @RequiredArgsConstructor
    static class InitMemberService {
        private final BankRepository bankRepository;
        private final CustomerAccountRepository customerAccountRepository;
        private final CustomerRepository customerRepository;
        private final ErrCodeRepository errCodeRepository;
        private final KakaoFriendRepository kakaoFriendRepository;
        private final CacheManager cacheManager;

        @Transactional
        public void initializationData() {
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
            bankRepository.saveAll(bankList);


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
            customerRepository.saveAll(customerList);


            // KakaoFriend
            List<KakaoFriend> kakaoFriendList = new ArrayList<>();

            KakaoFriend kakaoFriend = new KakaoFriend();
            kakaoFriend.setUserId("sampleId1");
            KakaoFriend kakaoFriend2 = new KakaoFriend();
            kakaoFriend2.setUserId("sampleId2");
            KakaoFriend kakaoFriend3 = new KakaoFriend();
            kakaoFriend3.setUserId("sampleId3");

            kakaoFriendList.add(kakaoFriend); kakaoFriendList.add(kakaoFriend2); kakaoFriendList.add(kakaoFriend3);
            kakaoFriendRepository.saveAll(kakaoFriendList);

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

            customerAccountRepository.save(customerAccount);
            customerAccountRepository.save(customerAccount2);
        }

        @Transactional
        public void initializationErrCode() {
            List<ErrCode> errCodeList = new ArrayList<>();

            ErrCode errCode100 = new ErrCode("100","Invalid Sender/Receiver Account.");
            ErrCode errCode200 = new ErrCode("200","TransferAmt must lower than balance in sender's account.");
            ErrCode errCode300 = new ErrCode("300","Could not find Sender/Receiver Account. Account must not be null.");
            ErrCode errCode400 = new ErrCode("400","Could not find Transfer Information");
            ErrCode errCode500 = new ErrCode("500","WAITING status Transfer Must be cancel/confirm.");

            errCodeList.add(errCode100); errCodeList.add(errCode200); errCodeList.add(errCode300); errCodeList.add(errCode400); errCodeList.add(errCode500);

            errCodeRepository.saveAll(errCodeList);
        }

        public void initializationCache() {
            cacheManager.getCache("transfer").clear();
        }
    }
}
