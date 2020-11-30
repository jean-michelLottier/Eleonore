package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class HealthOT extends EndPointOT {
    private String status;
    private Map<String, ComponentOT> components;

    public HealthOT() {
        super("health");
    }

    @Getter
    @Setter
    private static class ComponentOT {
        private String status;
        private Map<String, ComponentOT> components;
        private Map<String, Object> details;
    }
}
