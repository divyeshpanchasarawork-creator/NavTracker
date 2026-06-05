package com.divyesh.panchasara.NavTracker.exception;

import java.time.LocalDate;

public class InvalidNavDataException extends RuntimeException{
    public InvalidNavDataException(String fundCode, LocalDate beforeDate){
        super("NAV value for " + fundCode + " on " + beforeDate + " is zero or negative");
    }
}
