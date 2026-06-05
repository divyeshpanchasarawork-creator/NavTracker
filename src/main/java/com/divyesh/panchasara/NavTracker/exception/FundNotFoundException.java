package com.divyesh.panchasara.NavTracker.exception;

public class FundNotFoundException extends RuntimeException {
    public FundNotFoundException(String fundCode) {
        super("No NAV data found for fund code: " + fundCode);
    }
}
