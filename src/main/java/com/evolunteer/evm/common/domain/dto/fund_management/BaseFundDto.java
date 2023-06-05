package com.evolunteer.evm.common.domain.dto.fund_management;

import com.evolunteer.evm.common.domain.dto.address_management.AddressDto;
import com.evolunteer.evm.common.domain.enums.fund_management.FundActivityCategory;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
public class BaseFundDto {
    private Long id;
    @NotBlank(message = "Fund name must be specified!")
    private String name;
    @NotBlank(message = "Fund description must be specified!")
    private String description;
    @NotBlank(message = "Fund phone must be specified!")
    private String phone;
    @NotBlank(message = "Fund email must be specified!")
    private String email;
    @Valid
    private AddressDto address;
    @NotEmpty(message = "Categories must be specified!")
    private Set<FundActivityCategory> categories = new HashSet<>();

}
