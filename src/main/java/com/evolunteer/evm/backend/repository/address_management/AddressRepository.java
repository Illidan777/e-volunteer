package com.evolunteer.evm.backend.repository.address_management;

import com.evolunteer.evm.common.domain.entity.address_management.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
