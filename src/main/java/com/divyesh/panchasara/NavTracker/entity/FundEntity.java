package com.divyesh.panchasara.NavTracker.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"scheme_code", "nav_date"})
)
public class FundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String schemeCode;
    private String isinDivPayoutGrowth;
    private String isinDivReinvestment;
    private String schemeName;
    private Double netAssetValue;
    private LocalDate navDate;

    public FundEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getIsinDivPayoutGrowth() {
        return isinDivPayoutGrowth;
    }

    public void setIsinDivPayoutGrowth(String isinDivPayoutGrowth) {
        this.isinDivPayoutGrowth = isinDivPayoutGrowth;
    }

    public String getIsinDivReinvestment() {
        return isinDivReinvestment;
    }

    public void setIsinDivReinvestment(String isinDivReinvestment) {
        this.isinDivReinvestment = isinDivReinvestment;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
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
