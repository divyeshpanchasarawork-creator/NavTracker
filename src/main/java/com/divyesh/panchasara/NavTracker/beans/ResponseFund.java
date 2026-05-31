package com.divyesh.panchasara.NavTracker.beans;

import java.time.LocalDate;

public class ResponseFund {
    private String fundCode;
    private String fundName;
    private LocalDate navDate;
    private Double netAssetValue;

    public ResponseFund() {
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

    public Double getNetAssetValue() {
        return netAssetValue;
    }

    public void setNetAssetValue(Double netAssetValue) {
        this.netAssetValue = netAssetValue;
    }

    public LocalDate getNavDate() {
        return navDate;
    }
    public void setNavDate(LocalDate navDate) {
        this.navDate = navDate;
    }
}
