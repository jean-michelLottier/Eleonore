package com.dashboard.eleonore.element;

import com.dashboard.eleonore.dashboard.dto.DashboardDTO;
import com.dashboard.eleonore.dashboard.service.DashboardService;
import com.dashboard.eleonore.element.dto.ElementDTO;
import com.dashboard.eleonore.element.dto.SonarDTO;
import com.dashboard.eleonore.element.repository.entity.ElementType;
import com.dashboard.eleonore.element.service.ElementService;
import com.dashboard.eleonore.http.BaseController;
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
@RequestMapping("/dashboard/element")
public class ElementController extends BaseController {

    @Autowired
    private ElementService elementService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    public ElementController(ProfileService profileService) {
        super(profileService);
    }

    /**
     * Method to add a Sonar element in a dashboard
     *
     * @param request
     * @param dashboardIdStr
     * @param sonarDTO
     * @return
     */
    @PostMapping("/sonar")
    public ResponseEntity<ElementDTO> addSonarElement(HttpServletRequest request,
                                          @RequestParam(name = "dashboardId", required = true) String dashboardIdStr,
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

        ElementDTO savedElement = this.elementService.saveElement(dashboardId, sonarDTO);

        return ResponseEntity.ok(savedElement);
    }

    /**
     * Method to delete an element in a dashboard
     *
     * @param request
     * @param dashboardIdStr
     * @param type
     * @param elementIdStr
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteElement(HttpServletRequest request,
                                        @RequestParam(name = "dashboardId", required = true) String dashboardIdStr,
                                        @RequestParam(name = "type", required = true) String type,
                                        @RequestParam(name = "elementId", required = true) String elementIdStr) {
        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        ElementType elementType;
        try {
            elementType = ElementType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        this.elementService.deleteElement(profileDTO.getAuthentication().getProfileId(), Long.valueOf(dashboardIdStr),
                Long.valueOf(elementIdStr), elementType);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sonar/modify")
    public ResponseEntity modifySonarElement(HttpServletRequest request, @RequestBody SonarDTO sonarDTO) {
        if (sonarDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ProfileDTO profileDTO = checkSessionActive(request.getSession());

        if (this.elementService.isComponentEditable(profileDTO.getAuthentication().getProfileId(), sonarDTO.getId(), ElementType.SONAR)) {
            this.elementService.updateElement(sonarDTO);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }
}
