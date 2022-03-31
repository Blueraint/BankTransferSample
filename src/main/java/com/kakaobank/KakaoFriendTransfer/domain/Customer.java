package com.kakaobank.KakaoFriendTransfer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
/*
@SequenceGenerator(
        name = "CUSTOMER_ID_GENERATOR",
        sequenceName = "CUSTOMER_ID_SEQ",
        initialValue = 1,
        allocationSize = 100
)
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CUSTOMER")
public class Customer extends DateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "CUSTOMER_ID")
    private Long id;

    @Column(name = "CI")
    private String ci;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<CustomerAccount> customerAccountList = new ArrayList<>();

    public Customer(String ci, String name, String address) {
        this.ci = ci;
        this.name = name;
        this.address = address;
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.modifyDate = regDate;
    }
}
