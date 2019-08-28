package com.dashboard.eleonore.profile.dto;

import com.dashboard.eleonore.profile.repository.entity.Authentication;
import com.dashboard.eleonore.profile.repository.entity.User;

public class UserDTO extends ProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user, null);
    }

    public UserDTO(Authentication authentication) {
        this(null, authentication);
    }

    public UserDTO(User user, Authentication authentication) {
        if (user != null) {
            this.id = user.getId();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
        }
        if (authentication != null) {
            setAuthentication(new AuthenticationDTO(authentication));
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
