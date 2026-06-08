package com.divyesh.panchasara.NavTracker.service;

import com.divyesh.panchasara.NavTracker.dto.ResponseFund;
import com.divyesh.panchasara.NavTracker.dto.ResponseFundHistory;
import com.divyesh.panchasara.NavTracker.dto.ResponseFundReturns;
import com.divyesh.panchasara.NavTracker.entity.FundEntity;
import com.divyesh.panchasara.NavTracker.exception.FundNotFoundException;
import com.divyesh.panchasara.NavTracker.exception.InvalidDateRangeException;
import com.divyesh.panchasara.NavTracker.exception.InvalidNavDataException;
import com.divyesh.panchasara.NavTracker.repository.FundRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class NavServiceImpl implements NavService {

    private final FundRepository fundRepository;

    public NavServiceImpl(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    @Override
    public ResponseFund getLatest(String fundCode) {
        FundEntity fundEntity = fundRepository.getLatest(fundCode);

        if (fundEntity == null) throw new FundNotFoundException(fundCode);

        ResponseFund responseFund = new ResponseFund();
        responseFund.setFundCode(fundCode);
        responseFund.setFundName(fundEntity.getSchemeName());
        responseFund.setNavDate(fundEntity.getNavDate());
        responseFund.setNetAssetValue(fundEntity.getNetAssetValue());

        return responseFund;
    }

    @Override
    public ResponseFundHistory getHistory(String fundCode, LocalDate fromDate, LocalDate toDate) {
        if (!fromDate.isBefore(toDate)) throw new InvalidDateRangeException(fromDate, toDate);

        List<FundEntity> fundEntityList = fundRepository.getHistory(fundCode, fromDate, toDate);

        if (fundEntityList == null || fundEntityList.isEmpty()) throw new FundNotFoundException(fundCode);

        Map<LocalDate, BigDecimal> navHistory = new TreeMap<>();
        for (FundEntity fundEntity: fundEntityList) {
            navHistory.put(fundEntity.getNavDate(), fundEntity.getNetAssetValue());
        }

        FundEntity fundEntity = fundEntityList.getFirst();

        ResponseFundHistory responseFundHistory = new ResponseFundHistory();

        responseFundHistory.setFundCode(fundCode);
        responseFundHistory.setFundName(fundEntity.getSchemeName());
        responseFundHistory.setFromDate(fromDate);
        responseFundHistory.setToDate(toDate);
        responseFundHistory.setHistory(navHistory);

        return responseFundHistory;
    }

    @Override
    public ResponseFundReturns getReturns(String fundCode, LocalDate beforeDate, LocalDate afterDate) {
        if (!beforeDate.isBefore(afterDate)) throw new InvalidDateRangeException(beforeDate, afterDate);

        FundEntity beforeDateEntity = fundRepository.getNavOfDateOrLastUpdatedNav(fundCode, beforeDate);
        FundEntity afterDateEntity  = fundRepository.getNavOfDateOrLastUpdatedNav(fundCode, afterDate);

        if (beforeDateEntity == null || afterDateEntity == null) throw new FundNotFoundException(fundCode);

        BigDecimal beforeNav = beforeDateEntity.getNetAssetValue();
        BigDecimal afterNav = afterDateEntity.getNetAssetValue();

        if (beforeNav.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidNavDataException(fundCode, beforeDate);
        }

        ResponseFundReturns responseFundReturns = new ResponseFundReturns();
        responseFundReturns.setFundCode(fundCode);
        responseFundReturns.setFundName(beforeDateEntity.getSchemeName());
        responseFundReturns.setBeforeDate(beforeDate);
        responseFundReturns.setAfterDate(afterDate);

        BigDecimal returns = afterNav.subtract(beforeNav)
                .divide(beforeNav, 6, RoundingMode.HALF_UP);

        responseFundReturns.setReturns(returns.multiply(new BigDecimal(100)));

        return responseFundReturns;
    }
}
