package com.dashboard.eleonore.profile.dto;

import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;

public class AuthenticationDTO {
    private Long id;
    private Long profileId;
    private String login;
    private String password;
    private ProfileType type;

    public AuthenticationDTO() {
    }

    public AuthenticationDTO(Authentication authentication) {
        this.id = authentication.getId();
        this.profileId = authentication.getProfileId();
        this.login = authentication.getLogin();
        this.password = authentication.getPassword();
        this.type = authentication.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProfileType getType() {
        return type;
    }

    public void setType(ProfileType type) {
        this.type = type;
    }
}
