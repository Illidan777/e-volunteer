package com.evolunteer.evm.common.domain.entity.user_management;

import com.evolunteer.evm.common.domain.entity.file_management.FileMetaData;
import com.evolunteer.evm.common.domain.entity.fund_management.Fund;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
@EqualsAndHashCode(exclude = "fund")
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

    @ManyToOne
    @JoinColumn(name = "picture_id")
    private FileMetaData picture;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account accountDetails;

    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;
}
