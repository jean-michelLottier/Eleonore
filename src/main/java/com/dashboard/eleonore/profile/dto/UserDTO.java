package com.dashboard.eleonore.profile.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class UserDTO extends ProfileDTO implements Serializable {
    private static final long serialVersionUID = -1561865196638518396L;

    private Long id;
    private String firstName;
    private String lastName;
}
