package com.dashboard.eleonore.actuator.ot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ActuatorOT {
    @JsonProperty(value = "_links")
    private Map<String, LinkOT> links;
    private AuditEventsOT events;

    public AuditEventsOT getEvents() {
        return events;
    }

    public void setEvents(AuditEventsOT events) {
        this.events = events;
    }

    @Getter
    @Setter
    private static class LinkOT {
        private String href;
        private boolean templated;
    }
}
