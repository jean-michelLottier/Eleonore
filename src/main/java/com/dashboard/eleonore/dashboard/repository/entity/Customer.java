package com.dashboard.eleonore.dashboard.repository.entity;

import com.dashboard.eleonore.profile.repository.entity.ProfileType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public ProfileType getType() {
        return type;
    }

    public void setType(ProfileType type) {
        this.type = type;
    }
}
