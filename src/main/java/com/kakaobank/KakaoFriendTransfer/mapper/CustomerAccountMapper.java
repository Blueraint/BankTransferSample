package com.kakaobank.KakaoFriendTransfer.mapper;

import com.kakaobank.KakaoFriendTransfer.domain.CustomerAccount;
import com.kakaobank.KakaoFriendTransfer.domain.dto.CustomerAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerAccountMapper {
    CustomerAccountMapper INSTANCE = Mappers.getMapper(CustomerAccountMapper.class);

    @Mapping(source = "bank.bankCode", target = "bankCode")
    @Mapping(source = "bank.bankName", target = "bankName")
    @Mapping(source = "customer.name", target = "customerName")
    CustomerAccountDto entityToDto(CustomerAccount customerAccount);
}
