package com.evolunteer.evm.common.domain.entity.user_management;

import com.evolunteer.evm.common.domain.dto.file_management.EmbeddableFile;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fileName", column = @Column(name = "picture_file_name")),
            @AttributeOverride(name = "fileCode", column = @Column(name = "picture_file_code")),
    })
    private EmbeddableFile picture;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account accountDetails;
}
