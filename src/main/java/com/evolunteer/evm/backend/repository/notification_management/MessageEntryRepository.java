package com.evolunteer.evm.backend.repository.notification_management;

import com.evolunteer.evm.common.domain.entity.notification_management.MessageEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageEntryRepository extends JpaRepository<MessageEntry, Long> {
}
