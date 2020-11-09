package com.dashboard.eleonore.element.actuator;

import com.dashboard.eleonore.dashboard.dto.DashboardDTO;
import com.dashboard.eleonore.dashboard.service.DashboardService;
import com.dashboard.eleonore.element.actuator.dto.ActuatorDTO;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.BaseController;
import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping(value = "dashboard/element/actuator")
public class ActuatorEltController extends BaseController {
    private final ElementService<ActuatorDTO> elementService;
    private final DashboardService dashboardService;

    @Autowired
    public ActuatorEltController(ProfileService profileService, DashboardService dashboardService,
                                 ElementService<ActuatorDTO> elementService) {
        super(profileService);
        this.dashboardService = dashboardService;
        this.elementService = elementService;
    }

    /**
     * Method to add an actuator element in a dashboard
     *
     * @param request
     * @param dashboardIdStr
     * @param actuatorDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<ActuatorDTO> addElement(HttpServletRequest request,
                                                  @RequestParam(name = "dashboardId") String dashboardIdStr,
                                                  @RequestBody ActuatorDTO actuatorDTO) {
        if (StringUtils.isEmpty(dashboardIdStr) || actuatorDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ProfileDTO profileDTO = checkSessionActive(request.getSession());
        Long dashboardId = Long.valueOf(dashboardIdStr);
        Optional<DashboardDTO> optionalDashboardDTO = this.dashboardService.getDashboardById(dashboardId, profileDTO.getAuthentication().getProfileId());
        if (optionalDashboardDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.of(this.elementService.saveElement(dashboardId, actuatorDTO));
    }

    /**
     * Method to delete an actuator element in a dashboard
     *
     * @param request
     * @param dashboardIdStr
     * @param elementIdStr
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Object> deleteElement(HttpServletRequest request,
                                        @RequestParam(name = "dashboardId") String dashboardIdStr,
                                        @RequestParam(name = "elementId") String elementIdStr) {
        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        this.elementService.deleteElement(profileDTO.getAuthentication().getProfileId(), Long.valueOf(dashboardIdStr),
                Long.valueOf(elementIdStr));

        return ResponseEntity.ok().build();
    }

    /**
     * Method to modify an actuator element in a dashboard
     *
     * @param request
     * @param actuatorDTO
     * @return
     */
    @PostMapping("/modify")
    public ResponseEntity<ActuatorDTO> modifyElement(HttpServletRequest request, @RequestBody ActuatorDTO actuatorDTO) {
        if (actuatorDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        if (this.elementService.isComponentEditable(profileDTO.getAuthentication().getProfileId(), actuatorDTO.getId(), ElementType.ACTUATOR)) {
            return ResponseEntity.of(this.elementService.updateElement(actuatorDTO));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
