package com.divyesh.panchasara.NavTracker.service;

import com.divyesh.panchasara.NavTracker.dto.ResponseFund;
import com.divyesh.panchasara.NavTracker.dto.ResponseFundHistory;
import com.divyesh.panchasara.NavTracker.dto.ResponseFundReturns;

import java.time.LocalDate;

public interface NavService {
    ResponseFund getLatest(String fundCode);
    ResponseFundHistory getHistory(String fundCode, LocalDate fromDate, LocalDate toDate);
    ResponseFundReturns getReturns(String fundCode, LocalDate beforeDate, LocalDate afterDate);
}
