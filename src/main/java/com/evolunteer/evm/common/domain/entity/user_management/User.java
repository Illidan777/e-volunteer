package com.evolunteer.evm.common.domain.entity.user_management;

import com.evolunteer.evm.common.domain.entity.file_management.FileMetaData;
import com.evolunteer.evm.common.domain.entity.fund_management.Fund;
import com.evolunteer.evm.common.domain.entity.fund_management.FundTeamRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.EMPTY_STRING;
import static com.evolunteer.evm.common.utils.string.StringSymbolUtils.SPACE;

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

    @Where(clause = "fund_request_type = 'FUND_INVITATION'")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<FundTeamRequest> invitations = new HashSet<>();

    public String getFullName() {
        return StringUtils.normalizeSpace(
                (Objects.nonNull(this.surname) && !StringUtils.isBlank(this.surname) ? this.surname : EMPTY_STRING) + SPACE
                        + (Objects.nonNull(this.name) && !StringUtils.isBlank(this.name) ? this.name : EMPTY_STRING) + SPACE
                        + (Objects.nonNull(this.middleName) && !StringUtils.isBlank(this.middleName) ? this.middleName : EMPTY_STRING)
        );
    }
}
