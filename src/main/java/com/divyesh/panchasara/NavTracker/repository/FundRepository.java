package com.divyesh.panchasara.NavTracker.repository;

import com.divyesh.panchasara.NavTracker.entity.FundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FundRepository extends JpaRepository<FundEntity, UUID> {
    boolean existsByIsinDivPayoutGrowthAndNavDate(String isinDivPayoutGrowth, LocalDate navDate);

    @Query(value = "SELECT * from fund_entity f WHERE f.isin_div_payout_growth = :fundCode ORDER BY f.nav_date DESC LIMIT 1", nativeQuery = true)
    FundEntity getLatest(
            @Param("fundCode") String fundCode
    );

    @Query("SELECT f from FundEntity f WHERE f.isinDivPayoutGrowth = :fundCode AND f.navDate BETWEEN :fromDate AND :toDate ORDER BY f.navDate DESC")
    List<FundEntity> getHistory(
            @Param("fundCode") String fundCode,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query("SELECT f from FundEntity f WHERE f.isinDivPayoutGrowth = :fundCode AND f.navDate = :navDate")
    FundEntity getNavOfDate(
            @Param("fundCode") String fundCode,
            @Param("navDate") LocalDate navDate
    );

    @Query(value = "SELECT * from fund_entity f WHERE f.isin_div_payout_growth = :fundCode AND f.nav_date <= :navDate ORDER BY f.nav_date DESC LIMIT 1", nativeQuery = true)
    FundEntity getNavOfDateOrLastUpdatedNav(
            @Param("fundCode") String fundCode,
            @Param("navDate") LocalDate navDate
    );
}
