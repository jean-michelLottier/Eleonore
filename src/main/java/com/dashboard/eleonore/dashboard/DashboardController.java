package com.dashboard.eleonore.dashboard;

import com.dashboard.eleonore.dashboard.dto.CustomerDTO;
import com.dashboard.eleonore.dashboard.dto.DashboardDTO;
import com.dashboard.eleonore.dashboard.exception.DashboardNotFoundException;
import com.dashboard.eleonore.dashboard.service.DashboardService;
import com.dashboard.eleonore.http.BaseController;
import com.dashboard.eleonore.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {
    @Autowired
    private DashboardService dashboardService;

    @Autowired
    public DashboardController(ProfileService profileService) {
        super(profileService);
    }

    /**
     * Method to create a new dashboard
     *
     * @param request
     * @param dashboardDTO
     * @return
     */
    @PostMapping("/new")
    public ResponseEntity<DashboardDTO> create(HttpServletRequest request, @RequestBody DashboardDTO dashboardDTO) {
        var profileDTO = checkSessionActive(request.getSession());

        if (dashboardDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        dashboardDTO = this.dashboardService.saveDashboard(dashboardDTO);
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setDashboardId(dashboardDTO.getId());
        customerDTO.setProfileId(profileDTO.getAuthentication().getProfileId());
        customerDTO.setOwner(true);
        customerDTO.setEditable(true);
        customerDTO.setType(profileDTO.getAuthentication().getType());
        this.dashboardService.saveCustomer(customerDTO);

        return ResponseEntity.ok(dashboardDTO);
    }

    /**
     * Method to get dashboard information
     *
     * @param request
     * @param id
     * @param name
     * @return
     */
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(HttpServletRequest request,
                                                     @RequestParam(name = "id", required = false) String id,
                                                     @RequestParam(name = "name", required = false) String name) {
        var profileDTO = checkSessionActive(request.getSession());

        DashboardDTO dashboardDTO = null;
        if (!StringUtils.isEmpty(id)) {
            dashboardDTO = this.dashboardService.getDashboardById(Long.valueOf(id), profileDTO.getAuthentication().getProfileId())
                    .orElseThrow(() -> new DashboardNotFoundException(id));
        } else if (!StringUtils.isEmpty(name)) {
            dashboardDTO = this.dashboardService.getDashboardByName(name, profileDTO.getAuthentication().getProfileId())
                    .orElseThrow(() -> new DashboardNotFoundException(name));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(dashboardDTO);
    }

    /**
     * Method to get the dashboard list
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<DashboardDTO>> getDashboardList(HttpServletRequest request) {
        var profileDTO = checkSessionActive(request.getSession());

        return ResponseEntity.ok(this.dashboardService.getDashboards(profileDTO.getAuthentication().getProfileId()));
    }

    /**
     * Method to delete a dashboard
     *
     * @param request
     * @param id
     * @param name
     * @return
     */
    @DeleteMapping
    public ResponseEntity delete(HttpServletRequest request,
                                 @RequestParam(name = "id", required = false) String id,
                                 @RequestParam(name = "name", required = false) String name) {
        var profileDTO = checkSessionActive(request.getSession());

        if (!StringUtils.isEmpty(id)) {
            this.dashboardService.deleteDashboard(Long.valueOf(id), profileDTO.getAuthentication().getProfileId());
        } else if (!StringUtils.isEmpty(name)) {
            this.dashboardService.deleteDashboard(name, profileDTO.getAuthentication().getProfileId());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok().build();
    }
}
