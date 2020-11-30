package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ConfigPropsOT extends EndPointOT {
    private Map<String, ContextOT> contexts;

    public ConfigPropsOT() {
        super("configprops");
    }

    @Getter
    @Setter
    private static class ContextOT {
        private String parentId;
        private Map<String, BeanOT> beans;

        @Getter
        @Setter
        private static class BeanOT {
            private String prefix;
            private Map<String, Object> properties;
            private Map<String, Object> inputs;
        }
    }
}
