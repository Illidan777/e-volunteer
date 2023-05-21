package com.evolunteer.evm.common.domain.entity.file_management;

import com.evolunteer.evm.common.domain.enums.file_management.FileExtension;
import com.evolunteer.evm.common.domain.enums.file_management.FileStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files_meta_data")
@Data
@EntityListeners(AuditingEntityListener.class)
public class FileMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    private String path;

    @Enumerated(EnumType.STRING)
    private FileExtension fileExtension;

    @Enumerated(EnumType.STRING)
    private FileStatus status = FileStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isNonActive() {
        return !this.status.equals(FileStatus.ACTIVE);
    }

}
