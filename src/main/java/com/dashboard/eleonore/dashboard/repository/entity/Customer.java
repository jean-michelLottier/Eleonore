package com.dashboard.eleonore.dashboard.repository.entity;

import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = -6979305705417452745L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dashboard_id", nullable = false)
    private Long dashboardId;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "owner")
    private boolean owner;

    @Column(name = "editable")
    private boolean editable;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProfileType type;
}
