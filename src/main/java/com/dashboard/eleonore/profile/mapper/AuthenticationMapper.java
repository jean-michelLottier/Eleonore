package com.dashboard.eleonore.profile.mapper;

import com.dashboard.eleonore.profile.dto.AuthenticationDTO;
import com.dashboard.eleonore.profile.repository.entity.Authentication;
import org.mapstruct.Mapper;

@Mapper
public interface AuthenticationMapper {
    AuthenticationDTO authenticationToAuthenticationDTO(Authentication authentication);

    Authentication authenticationDTOToAuthentication(AuthenticationDTO authenticationDTO);
}
