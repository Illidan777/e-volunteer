package com.evolunteer.evm.backend.repository.fund_management;

import com.evolunteer.evm.common.domain.entity.fund_management.FundRequisite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface FundRequisiteRepository extends JpaRepository<FundRequisite, Long> {
    @Transactional
    @Modifying
    @Query("delete from FundRequisite cs where cs.id = :id")
    void deleteById(@Param("id") Long id);
}
