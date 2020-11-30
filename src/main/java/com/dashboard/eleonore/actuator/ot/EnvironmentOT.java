package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnvironmentOT extends EndPointOT {
    private String[] activeProfiles;
    private PropertySourceOT[] propertySources;

    public EnvironmentOT() {
        super("env");
    }

    @Getter
    @Setter
    private static class PropertySourceOT {
        private String name;
        private Map<String, PropertyOT> properties;

        @Getter
        @Setter
        private static class PropertyOT {
            private String value;
            private String origin;
        }
    }
}
