package com.evolunteer.evm.common.domain.dto.fund_management;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FundRequisiteDto {
    private Long id;
    @NotBlank(message = "Recipient must be specified!")
    private String recipient;
    @NotBlank(message = "Bank must be specified!")
    private String bank;
    @NotBlank(message = "Bank code must be specified!")
    private String bankCode;
    private String iban;
    @NotBlank(message = "Payment account must be specified!")
    private String paymentAccount;
    private String swiftCode;
    private String legalAddress;
    private String paymentLink;
}
