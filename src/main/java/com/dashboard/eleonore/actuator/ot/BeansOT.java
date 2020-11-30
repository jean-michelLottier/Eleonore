package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BeansOT extends EndPointOT {
    private Map<String, ContextOT> contexts;

    public BeansOT() {
        super("beans");
    }

    @Getter
    @Setter
    private static class ContextOT {
        private String parentId;
        private Map<String, BeanOT> beans;

        @Getter
        @Setter
        private static class BeanOT {
            private String[] aliases;
            private String scope;
            private String type;
            private String resource;
            private String[] dependencies;
        }
    }
}
