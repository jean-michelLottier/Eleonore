package com.dashboard.eleonore.dashboard.dto;

import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private Long dashboardId;
    private Long profileId;
    private boolean owner;
    private boolean editable;
    private ProfileType type;
}
