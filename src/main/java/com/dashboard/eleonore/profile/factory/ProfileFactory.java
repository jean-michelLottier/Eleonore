package com.dashboard.eleonore.profile.factory;

import com.dashboard.eleonore.profile.dto.ProfileDTO;
import com.dashboard.eleonore.profile.dto.UserDTO;
import com.dashboard.eleonore.profile.mapper.AuthenticationMapper;
import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import org.mapstruct.factory.Mappers;

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

        AuthenticationMapper authenticationMapper = Mappers.getMapper(AuthenticationMapper.class);
        profileDTO.setAuthentication(authenticationMapper.authenticationToAuthenticationDTO(authentication));

        return profileDTO;
    }
}
