package com.dashboard.eleonore.profile.repository.entity;

import com.dashboard.eleonore.profile.dto.AuthenticationDTO;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Authentication implements Serializable {
    private static final long serialVersionUID = 4970750145768217896L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProfileType type;

    public Authentication() {
    }

    public Authentication(AuthenticationDTO authentication) {
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
