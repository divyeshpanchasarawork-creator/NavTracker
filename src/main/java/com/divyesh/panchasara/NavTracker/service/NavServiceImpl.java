package com.divyesh.panchasara.NavTracker.service;

import com.divyesh.panchasara.NavTracker.beans.ResponseFund;
import com.divyesh.panchasara.NavTracker.beans.ResponseFundHistory;
import com.divyesh.panchasara.NavTracker.beans.ResponseFundReturns;
import com.divyesh.panchasara.NavTracker.entity.FundEntity;
import com.divyesh.panchasara.NavTracker.repository.FundRepository;
import org.springframework.stereotype.Service;

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

        if (fundEntity == null) return null;

        ResponseFund responseFund = new ResponseFund();
        responseFund.setFundCode(fundCode);
        responseFund.setFundName(fundEntity.getSchemeName());
        responseFund.setNavDate(fundEntity.getNavDate());
        responseFund.setNetAssetValue(fundEntity.getNetAssetValue());

        return responseFund;
    }

    @Override
    public ResponseFundHistory getHistory(String fundCode, LocalDate fromDate, LocalDate toDate) {
        List<FundEntity> fundEntityList = fundRepository.getHistory(fundCode, fromDate, toDate);

        if (fundEntityList == null || fundEntityList.isEmpty()) return null;

        Map<LocalDate, Double> navHistory = new TreeMap<>();
        for (FundEntity fundEntity: fundEntityList) {
            navHistory.put(fundEntity.getNavDate(), fundEntity.getNetAssetValue());
        }

        FundEntity fundEntity = fundEntityList.getFirst();

        ResponseFundHistory responseFundHistory = new ResponseFundHistory();

        responseFundHistory.setFundCode(fundEntity.getSchemeCode());
        responseFundHistory.setFundName(fundEntity.getSchemeName());
        responseFundHistory.setFromDate(fromDate);
        responseFundHistory.setToDate(toDate);
        responseFundHistory.setHistory(navHistory);

        return responseFundHistory;
    }

    @Override
    public ResponseFundReturns getReturns(String fundCode, LocalDate beforeDate, LocalDate afterDate) {
        FundEntity beforeDateEntity = fundRepository.getNavOfDate(fundCode, beforeDate);
        FundEntity afterDateEntity = fundRepository.getNavOfDate(fundCode, afterDate);

        if (beforeDateEntity == null || afterDateEntity == null) return null;

        ResponseFundReturns responseFundReturns = new ResponseFundReturns();

        responseFundReturns.setFundCode(fundCode);
        responseFundReturns.setFundName(beforeDateEntity.getSchemeName());
        responseFundReturns.setBeforeDate(beforeDate);
        responseFundReturns.setAfterDate(afterDate);

        double returns = ((afterDateEntity.getNetAssetValue() - beforeDateEntity.getNetAssetValue()) * 100 )/ beforeDateEntity.getNetAssetValue();
        returns = Math.round(returns * 10000.0) / 10000.0;

        responseFundReturns.setReturns(returns);

        return responseFundReturns;
    }
}
