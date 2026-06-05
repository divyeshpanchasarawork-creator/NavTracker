package com.divyesh.panchasara.NavTracker.exception;

import java.time.LocalDate;

public class InvalidDateRangeException extends RuntimeException{
    public InvalidDateRangeException(LocalDate before, LocalDate after) {
        super("beforeDate/fromDate " + before + " must be strictly before afterDate/toDate " + after);
    }
}
