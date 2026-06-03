package com.divyesh.panchasara.NavTracker.beans;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ResponseFundHistory {
    private String fundCode;
    private String fundName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Map<LocalDate, BigDecimal> history;

    public ResponseFundHistory() {
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Map<LocalDate, BigDecimal> getHistory() {
        return history;
    }

    public void setHistory(Map<LocalDate, BigDecimal> history) {
        this.history = history;
    }
}
