package com.evolunteer.evm.backend.repository.file_management;

import com.evolunteer.evm.common.domain.entity.file_management.FileMetaData;
import com.evolunteer.evm.common.domain.enums.file_management.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {
    Optional<FileMetaData> findByCode(String code);

    @Transactional
    @Modifying
    @Query("update FileMetaData f set f.status = :status where f.code = :code")
    void updateFileStatusByCode(@Param("status") FileStatus status, @Param("code") String code);
}
