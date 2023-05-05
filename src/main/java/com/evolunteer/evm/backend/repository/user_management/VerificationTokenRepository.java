package com.evolunteer.evm.backend.repository.user_management;

import com.evolunteer.evm.common.domain.entity.user_management.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
