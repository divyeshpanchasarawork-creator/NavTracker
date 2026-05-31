package com.divyesh.panchasara.NavTracker.beans;

import java.time.LocalDate;

public class ResponseFundReturns {
    private String fundCode;
    private String fundName;
    private LocalDate beforeDate;
    private LocalDate afterDate;
    private double returns;

    public ResponseFundReturns() {
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

    public LocalDate getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(LocalDate beforeDate) {
        this.beforeDate = beforeDate;
    }

    public LocalDate getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(LocalDate afterDate) {
        this.afterDate = afterDate;
    }

    public double getReturns() {
        return returns;
    }

    public void setReturns(double returns) {
        this.returns = returns;
    }
}
