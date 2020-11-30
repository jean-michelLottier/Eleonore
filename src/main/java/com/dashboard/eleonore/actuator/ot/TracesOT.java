package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class TracesOT extends EndPointOT {
    private TraceOT[] traces;

    public TracesOT() {
        super("httptrace");
    }

    @Getter
    @Setter
    private static class TraceOT {
        private Date timestamp;
        private long timeTaken;
        private PrincipalOT principal;
        private RequestOT request;
        private ResponseOT response;
        private SessionOT session;

        @Getter
        @Setter
        private static class PrincipalOT {
            private String name;
        }

        @Getter
        @Setter
        private static class RequestOT {
            private String method;
            private String remoteAddress;
            private String uri;
            private Map<String, String[]> headers;
        }

        @Getter
        @Setter
        private static class ResponseOT {
            private int status;
            private Map<String, String[]> headers;
        }

        @Getter
        @Setter
        private static class SessionOT {
            private String id;
        }
    }
}
