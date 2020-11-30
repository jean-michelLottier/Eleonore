package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CachesOT extends EndPointOT {
    private Map<String, CacheManagerOT> cacheManagers;

    public CachesOT() {
        super("caches");
    }

    @Getter
    @Setter
    private static class CacheManagerOT {
        private Map<String, CacheOT> caches;

        @Getter
        @Setter
        private static class CacheOT {
            private String target;
        }
    }
}
