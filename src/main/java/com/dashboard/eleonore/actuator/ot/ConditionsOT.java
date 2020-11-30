package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ConditionsOT extends EndPointOT {
    private Map<String, ContextOT> contexts;

    public ConditionsOT() {
        super("conditions");
    }

    @Getter
    @Setter
    private static class ContextOT {
        private String parentId;
        private Map<String,MatchOT[]> positiveMatches;
        private Map<String, NegativeMatchOT> negativeMatches;
        private String[] unconditionalClasses;

        @Getter
        @Setter
        private static class NegativeMatchOT {
            private MatchOT[] matched;
            private MatchOT[] notMatched;
        }

        @Getter
        @Setter
        private static class MatchOT {
            private String condition;
            private String message;
        }
    }
}
