package com.dashboard.eleonore.dashboard.exception;

public class DashboardNotFoundException extends RuntimeException {
    public DashboardNotFoundException(String value) {
        super("Could not find dashboard " + value);
    }
}
