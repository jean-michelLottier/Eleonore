package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThreadDumpOT extends EndPointOT {
    private ThreadOT[] threads;

    public ThreadDumpOT() {
        super("threaddump");
    }

    @Getter
    @Setter
    private static class ThreadOT {
        private long blockedCount;
        private long blockedTime;
        private boolean daemon;
        private boolean inNative;
        private String lockName;
        private LockInfoOT lockInfo;
        private LockedMonitorOT[] lockedMonitors;
        private LockInfoOT[] lockedSynchronizers;
        private long lockOwnerId;
        private String lockOwnerName;
        private int priority;
        private StackTraceOT[] stackTrace;
        private boolean suspended;
        private long threadId;
        private String threadName;
        private String threadState;
        private int waitedCount;
        private long waitedTime;

        @Getter
        @Setter
        private static class LockInfoOT {
            private String className;
            private int identityHashCode;
        }

        @Getter
        @Setter
        private static class LockedMonitorOT {
            private String className;
            private int identityHashCode;
            private int lockedStackDepth;
            private Object lockedStackFrame;
        }

        @Getter
        @Setter
        private static class StackTraceOT {
            private String classLoaderName;
            private String className;
            private String fileName;
            private int lineNumber;
            private String methodName;
            private String moduleName;
            private String moduleVersion;
            private boolean nativeMethod;
        }
    }
}
