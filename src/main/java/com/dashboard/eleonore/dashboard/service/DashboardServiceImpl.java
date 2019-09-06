package com.dashboard.eleonore.dashboard.service;

import com.dashboard.eleonore.dashboard.repository.entity.Dashboard;
import com.dashboard.eleonore.dashboard.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Override
    public Dashboard createDashboard(Dashboard dashboard) {
        return this.dashboardRepository.save(dashboard);
    }

    @Override
    public Optional<Dashboard> getById(Long id) {
        return this.dashboardRepository.findById(id);
    }

    @Override
    public Optional<Dashboard> getByName(String name) {
        return this.dashboardRepository.findByName(name);
    }
}
