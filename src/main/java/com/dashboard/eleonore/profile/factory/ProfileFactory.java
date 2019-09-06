package com.dashboard.eleonore.profile.factory;

import com.dashboard.eleonore.profile.dto.AuthenticationDTO;
import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.dto.UserDTO;
import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;

public final class ProfileFactory {
    private ProfileFactory() {
    }

    public static ProfileDTO getProfile(ProfileType profileType) {
        switch (profileType) {
            case USER:
                return new UserDTO();
            case PROJECT:
            case ORGANIZATION:
            default:
                return null;
        }
    }

    public static ProfileDTO getProfile(Authentication authentication) {
        ProfileDTO profileDTO = ProfileFactory.getProfile(authentication.getType());
        if (profileDTO == null) {
            return null;
        }

        profileDTO.setAuthentication(new AuthenticationDTO(authentication));

        return profileDTO;
    }
}
