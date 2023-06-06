package com.evolunteer.evm.common.domain.dto.fund_management;

import com.evolunteer.evm.common.domain.enums.fund_management.FundHelpRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class FundHelpRequestDto {
    private Long id;
    private String number;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private FundHelpRequestStatus status;
    private HelpRequestExecutorDto executor;

    public String getFormattedCreateAt() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH::mm:ss").format(this.createdAt);
    }
}
