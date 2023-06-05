package com.evolunteer.evm.common.domain.entity.fund_management;

import com.evolunteer.evm.common.domain.entity.address_management.Address;
import com.evolunteer.evm.common.domain.entity.user_management.User;
import com.evolunteer.evm.common.domain.enums.fund_management.FundActivityCategory;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;


@Entity
@Data
@Table(name = "funds")
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private String phone;

    private String email;

    @OneToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "fund", cascade = ALL, fetch = EAGER)
    private Set<User> employees = new HashSet<>();

    @OneToMany(mappedBy = "fund", cascade = ALL, fetch = EAGER)
    private Set<FundRequisite> requisites = new HashSet<>();

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "fund_categories", joinColumns = @JoinColumn(name = "fund_id"))
    private Set<FundActivityCategory> categories = new HashSet<>();

    @Where(clause = "fund_request_type = 'USER_REQUEST'")
    @OneToMany(mappedBy = "fund", cascade = CascadeType.ALL, fetch = EAGER)
    private Set<FundTeamRequest> requests = new HashSet<>();
}
