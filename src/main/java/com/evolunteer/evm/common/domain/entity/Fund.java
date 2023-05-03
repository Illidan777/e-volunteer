package com.evolunteer.evm.common.domain.entity;

import com.evolunteer.evm.common.domain.enums.FundActivityCategory;
import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(cascade = ALL, fetch = EAGER)
    @JoinTable(name = "fund_employees",
            joinColumns = @JoinColumn(name = "fond_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> employees = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "fund_categories", joinColumns = @JoinColumn(name = "fund_id"))
    private Set<FundActivityCategory> categories = new HashSet<>();
}
