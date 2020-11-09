package com.dashboard.eleonore;

import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.exception.AuthenticationException;
import com.dashboard.eleonore.profile.exception.ProfileNotFoundException;
import com.dashboard.eleonore.profile.service.ProfileService;
import com.dashboard.eleonore.profile.service.ProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

public abstract class BaseController {
    private ProfileService profileService;

    public BaseController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Autowired
    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public ProfileService getProfileService() {
        return this.profileService;
    }

    public ProfileDTO checkSessionActive(HttpSession session) {
        if (session == null
                || !this.profileService.isTokenValid((String) session.getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY))) {
            throw new AuthenticationException();
        }

        return this.profileService.getProfile((String) session.getAttribute(ProfileServiceImpl.AUTH_TOKEN_KEY))
                .orElseThrow(ProfileNotFoundException::new);
    }
}
