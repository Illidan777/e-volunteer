package com.evolunteer.evm.backend.repository.user_management;

import com.evolunteer.evm.common.domain.entity.user_management.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
}
