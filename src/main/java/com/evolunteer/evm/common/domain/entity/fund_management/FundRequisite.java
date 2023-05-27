package com.evolunteer.evm.common.domain.entity.fund_management;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@Table(name = "funds_requisites")
@EqualsAndHashCode(exclude = "fund")
public class FundRequisite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;

    private String bank;

    private String bankCode;

    private String iban;

    private String paymentAccount;

    private String swiftCode;

    private String legalAddress;

    private String paymentLink;

    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;
}
