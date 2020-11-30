package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EndPointOT {
    private String name;

    public EndPointOT(String name) {
        this.name = name;
    }
}
