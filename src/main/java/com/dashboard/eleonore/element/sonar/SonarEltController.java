package com.dashboard.eleonore.element.sonar;

import com.dashboard.eleonore.dashboard.dto.DashboardDTO;
import com.dashboard.eleonore.dashboard.service.DashboardService;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.element.sonar.dto.SonarDTO;
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
@RequestMapping("/dashboard/element/sonar")
public class SonarEltController extends BaseController {

    private final ElementService<SonarDTO> elementService;
    private final DashboardService dashboardService;

    @Autowired
    public SonarEltController(ProfileService profileService, DashboardService dashboardService,
                              ElementService<SonarDTO> elementService) {
        super(profileService);
        this.dashboardService = dashboardService;
        this.elementService = elementService;
    }

    /**
     * Method to add a sonar element in a dashboard
     *
     * @param request
     * @param dashboardIdStr
     * @param sonarDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<SonarDTO> addElement(HttpServletRequest request,
                                               @RequestParam(name = "dashboardId") String dashboardIdStr,
                                               @RequestBody SonarDTO sonarDTO) {
        if (StringUtils.isEmpty(dashboardIdStr) || sonarDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ProfileDTO profileDTO = checkSessionActive(request.getSession());
        Long dashboardId = Long.valueOf(dashboardIdStr);
        Optional<DashboardDTO> optionalDashboardDTO = this.dashboardService.getDashboardById(dashboardId, profileDTO.getAuthentication().getProfileId());
        if (optionalDashboardDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.of(this.elementService.saveElement(dashboardId, sonarDTO));
    }

    /**
     * Method to delete a sonar element in a dashboard
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

        this.elementService.deleteElement(profileDTO.getAuthentication().getProfileId(), Long.valueOf(dashboardIdStr), Long.valueOf(elementIdStr));

        return ResponseEntity.ok().build();
    }

    /**
     * Method to modify a sonar element in a dashboard
     *
     * @param request
     * @param sonarDTO
     * @return
     */
    @PostMapping("/modify")
    public ResponseEntity<SonarDTO> modifyElement(HttpServletRequest request, @RequestBody SonarDTO sonarDTO) {
        if (sonarDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        if (this.elementService.isComponentEditable(profileDTO.getAuthentication().getProfileId(), sonarDTO.getId(), ElementType.SONAR)) {
            return ResponseEntity.of(this.elementService.updateElement(sonarDTO));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
