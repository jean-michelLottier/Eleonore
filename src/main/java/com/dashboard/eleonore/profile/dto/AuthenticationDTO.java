package com.dashboard.eleonore.profile.dto;

import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AuthenticationDTO implements Serializable {
    private static final long serialVersionUID = -1053002746597912174L;

    private Long id;
    private Long profileId;
    private String login;
    private String password;
    private ProfileType type;
}
