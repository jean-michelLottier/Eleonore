package com.dashboard.eleonore.dashboard.service;

import com.dashboard.eleonore.dashboard.repository.entity.Dashboard;

import java.util.Optional;

public interface DashboardService {

    Dashboard createDashboard(Dashboard dashboard);

    Optional<Dashboard> getById(Long id);

    Optional<Dashboard> getByName(String name);
}
