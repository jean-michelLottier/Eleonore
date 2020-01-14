package com.dashboard.eleonore.profile;

import com.dashboard.eleonore.profile.dto.AuthenticationDTO;
import com.dashboard.eleonore.profile.dto.UserDTO;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import com.dashboard.eleonore.profile.service.ProfileService;
import com.dashboard.eleonore.profile.service.ProfileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
public class ProfileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    /**
     * Method for a profile to log in to Eleonore
     * @param request
     * @param authenticationDTO
     * @return
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity login(HttpServletRequest request, @RequestBody AuthenticationDTO authenticationDTO) {
        LOGGER.info("eleonore - Attempt for login");
        if (request.getSession().getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY) != null) {
            LOGGER.info("eleonore - User already logged in");
            return ResponseEntity.ok().build();
        }

        ResponseEntity response = null;
        var optionalAuth = this.profileService.getAuthentication(authenticationDTO.getLogin(), authenticationDTO.getPassword());
        if (optionalAuth.isPresent()) {
            var authentication = optionalAuth.get();
            // We get profile information which it will be returned for the client
            var optionalProfileDTO = this.profileService.getProfile(authentication.getId(), authentication.getType());
            // Save token in session and in databases
            var token = ProfileService.generateToken(authentication);
            request.getSession().setAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY, token);
            this.profileService.saveToken(authentication, token);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccessControlExposeHeaders(Arrays.asList("X-Auth-Token"));
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
    public ResponseEntity logout(HttpServletRequest request) {
        LOGGER.info("eleonore - Attempt for logout");
        if (request.getSession().getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY) != null) {
            this.profileService.deleteToken((String) request.getSession().getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY));
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
    public ResponseEntity createUser(HttpServletRequest request, @RequestBody UserDTO user) {
        LOGGER.info("eleonore - Attempt for user creation");
        // TODO : Check if the user already exists
        return ResponseEntity.ok(this.profileService.saveProfile(user, ProfileType.USER));
    }
}
