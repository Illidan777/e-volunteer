package com.evolunteer.evm.backend.repository.user_management;

import com.evolunteer.evm.common.domain.entity.user_management.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
