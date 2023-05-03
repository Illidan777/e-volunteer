package com.evolunteer.evm.common.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String middleName;

    private String phone;

    private String email;

    private Date birthDate;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account accountDetails;
}
