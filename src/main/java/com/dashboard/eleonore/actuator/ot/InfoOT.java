package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class InfoOT extends EndPointOT {
    private GitOT git;
    private BuildOT build;

    public InfoOT() {
        super("info");
    }

    @Getter
    @Setter
    private static class BuildOT {
        private String artifact;
        private String group;
        private String name;
        private String version;
        private Date time;
    }

    @Getter
    @Setter
    public static class GitOT {
        private String branch;
        private CommitOT commit;

        @Getter
        @Setter
        private static class CommitOT {
            private String id;
            private Date time;
        }
    }
}
