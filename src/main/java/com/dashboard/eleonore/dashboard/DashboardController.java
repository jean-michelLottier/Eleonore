package com.dashboard.eleonore.dashboard;

import com.dashboard.eleonore.dashboard.exception.DashboardNotFoundException;
import com.dashboard.eleonore.dashboard.repository.Dashboard;
import com.dashboard.eleonore.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @PostMapping("/new")
    public Dashboard create(@RequestBody Dashboard dashboard) {
        return this.dashboardService.createDashboard(dashboard);
    }

    @GetMapping("/id/{id}")
    public Dashboard getById(@PathVariable String id) {
        return this.dashboardService.getById(Long.valueOf(id)).orElseThrow(() -> new DashboardNotFoundException(id));
    }

    @GetMapping("/name/{name}")
    public Dashboard getByName(@PathVariable String name) {
        return this.dashboardService.getByName(name).orElseThrow(() -> new DashboardNotFoundException(name));
    }
}
