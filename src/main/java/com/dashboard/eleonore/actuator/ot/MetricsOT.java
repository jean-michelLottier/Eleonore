package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetricsOT extends EndPointOT {
    private String[] names;

    public MetricsOT() {
        super("metrics");
    }
}
