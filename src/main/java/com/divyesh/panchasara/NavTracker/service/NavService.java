package com.divyesh.panchasara.NavTracker.service;

import com.divyesh.panchasara.NavTracker.beans.ResponseFund;
import com.divyesh.panchasara.NavTracker.beans.ResponseFundHistory;
import com.divyesh.panchasara.NavTracker.beans.ResponseFundReturns;

import java.time.LocalDate;

public interface NavService {
    ResponseFund getLatest(String fundCode);
    ResponseFundHistory getHistory(String fundCode, LocalDate fromDate, LocalDate toDate);
    ResponseFundReturns getReturns(String funcCode, LocalDate beforeDate, LocalDate afterDate);
}
