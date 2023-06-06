package com.evolunteer.evm.common.domain.entity.fund_management;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "help_request_executors")
public class HelpRequestExecutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String middleName;

    private String organizationName;

    private String phone;

    private String email;
}
