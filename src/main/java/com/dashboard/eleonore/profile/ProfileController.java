package com.dashboard.eleonore.profile;

import com.dashboard.eleonore.BaseController;
import com.dashboard.eleonore.profile.dto.AuthenticationDTO;
import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.dto.UserDTO;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import com.dashboard.eleonore.profile.service.ProfileService;
import com.dashboard.eleonore.profile.service.ProfileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@Slf4j
public class ProfileController extends BaseController {

    @Autowired
    public ProfileController(ProfileService profileService) {
        super(profileService);
    }

    /**
     * Method for a profile to log in to Eleonore
     * @param request
     * @param authenticationDTO
     * @return
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<ProfileDTO> login(HttpServletRequest request, @RequestBody AuthenticationDTO authenticationDTO) {
        log.info("eleonore - Attempt for login");
        if (request.getSession().getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY) != null) {
            log.info("eleonore - User already logged in");
            return ResponseEntity.ok().build();
        }

        ResponseEntity<ProfileDTO> response;
        var optionalAuth = getProfileService().getAuthentication(authenticationDTO.getLogin(), authenticationDTO.getPassword());
        if (optionalAuth.isPresent()) {
            var authentication = optionalAuth.get();
            // We get profile information which it will be returned for the client
            var optionalProfileDTO = getProfileService().getProfile(authentication.getId(), authentication.getType());
            // Save token in session and in databases
            var token = ProfileService.generateToken(authentication);
            request.getSession().setAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY, token);
            getProfileService().saveToken(authentication, token);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccessControlExposeHeaders(Collections.singletonList("X-Auth-Token"));
            httpHeaders.setAccessControlAllowCredentials(true);

            if (optionalProfileDTO.isPresent()) {
                response = ResponseEntity.ok().headers(httpHeaders).body(optionalProfileDTO.get());
            } else {
                response = ResponseEntity.ok().headers(httpHeaders).build();
            }
        } else {
            request.getSession().invalidate();
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return response;
    }

    /**
     * Method for a profile to log out to Eleonore
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        log.info("eleonore - Attempt for logout");
        if (request.getSession().getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY) != null) {
            getProfileService().deleteToken((String) request.getSession().getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY));
            request.getSession().invalidate();
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Method to create a new user
     * @param request
     * @param user
     * @return
     */
    @PostMapping(value = "/user/create", consumes = "application/json")
    public ResponseEntity<ProfileDTO> createUser(HttpServletRequest request, @RequestBody UserDTO user) {
        log.info("eleonore - Attempt for user creation");
        // TODO : Check if the user already exists
        return ResponseEntity.ok(getProfileService().saveProfile(user, ProfileType.USER));
    }
}
