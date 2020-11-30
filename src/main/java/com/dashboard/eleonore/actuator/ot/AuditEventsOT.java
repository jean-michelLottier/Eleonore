package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AuditEventsOT extends EndPointOT {
    List<EventOT> events;

    public AuditEventsOT() {
        super("auditevents");
    }

    private static class EventOT {
        @Getter
        @Setter
        private Date timestamp;
        @Getter
        @Setter
        private String principal;
        @Getter
        @Setter
        private String type;
    }
}
