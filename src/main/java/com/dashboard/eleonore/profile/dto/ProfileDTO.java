package com.dashboard.eleonore.profile.dto;

public abstract class ProfileDTO {
    private AuthenticationDTO authentication;

    public AuthenticationDTO getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationDTO authentication) {
        this.authentication = authentication;
    }
}
