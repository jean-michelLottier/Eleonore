package com.dashboard.eleonore.profile.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "token")
public class AuthToken implements Serializable {
    private static final long serialVersionUID = -1967944556134225181L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "authentication_id", nullable = false)
    private Long authenticationId;

    @Column(name = "auth_token", nullable = false, length = 512)
    private String token;

    @Column(name = "created_date")
    private LocalDateTime createdDateTime;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDateTime;
}
