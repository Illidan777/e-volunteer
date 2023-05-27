package com.evolunteer.evm.common.domain.dto.address_management;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressDto {
    private Long id;
    @NotBlank(message = "Country must be specified!")
    private String country;
    @NotBlank(message = "Region must be specified!")
    private String region;
    @NotBlank(message = "City must be specified!")
    private String city;
    @NotBlank(message = "Street must be specified!")
    private String street;
    @NotBlank(message = "House must be specified!")
    private String house;
    private String corpus;
    private String office;
    private String postIndex;
}
