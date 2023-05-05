package com.evolunteer.evm.common.domain.entity.address_management;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;

    private String region;

    private String city;

    private String street;

    private String house;

    private String corpus;

    private String office;
}
