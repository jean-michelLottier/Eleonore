package com.dashboard.eleonore.dashboard.dto;

import com.dashboard.eleonore.dashboard.repository.entity.Customer;
import com.dashboard.eleonore.profile.repository.entity.ProfileType;

public class CustomerDTO {
    private Long id;
    private Long dashboardId;
    private Long profileId;
    private boolean owner;
    private boolean editable;
    private ProfileType type;

    public CustomerDTO(){}

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.dashboardId = customer.getDashboardId();
        this.profileId = customer.getProfileId();
        this.owner = customer.isOwner();
        this.editable = customer.isEditable();
        this.type = customer.getType();
    }

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
