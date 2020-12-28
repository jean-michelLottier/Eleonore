package com.dashboard.eleonore.dashboard.mapper;

import com.dashboard.eleonore.dashboard.dto.DashboardDTO;
import com.dashboard.eleonore.dashboard.repository.entity.Dashboard;
import org.mapstruct.Mapper;

@Mapper
public interface DashboardMapper {
    DashboardDTO dashboardToDashboardDTO(Dashboard dashboard);

    Dashboard dashboardDTOToDashboard(DashboardDTO dashboardDTO);
}
