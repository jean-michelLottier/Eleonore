package com.dashboard.eleonore.profile.mapper;

import com.dashboard.eleonore.profile.dto.UserDTO;
import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    User userDTOToUser(UserDTO userDTO);

    default UserDTO userToUserDTO(User user, Authentication authentication) {
        UserDTO userDTO = new UserDTO();
        if (user != null) {
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
        }
        if (authentication != null) {
            AuthenticationMapper mapper = Mappers.getMapper(AuthenticationMapper.class);
            userDTO.setAuthentication(mapper.authenticationToAuthenticationDTO(authentication));
        }

        return userDTO;
    }

    default UserDTO userToUserDTO(User user) {
        return userToUserDTO(user, null);
    }
}
