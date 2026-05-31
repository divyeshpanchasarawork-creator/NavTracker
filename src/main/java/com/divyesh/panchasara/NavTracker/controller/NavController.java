package com.divyesh.panchasara.NavTracker.controller;

import com.divyesh.panchasara.NavTracker.beans.ResponseFund;
import com.divyesh.panchasara.NavTracker.beans.ResponseFundHistory;
import com.divyesh.panchasara.NavTracker.beans.ResponseFundReturns;
import com.divyesh.panchasara.NavTracker.service.NavService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/funds")
public class NavController {

    private final NavService service;

    public NavController(NavService service) {
        this.service = service;
    }

    @GetMapping("/{fundCode}/latest-nav")
    public ResponseEntity<ResponseFund> getLatestNav(
            @PathVariable String fundCode
    ) {
        ResponseFund responseFund = service.getLatest(fundCode);

        if (responseFund == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(responseFund);
    }

    @GetMapping("/{fundCode}/history")
    public ResponseEntity<ResponseFundHistory> getHistory(
            @PathVariable String fundCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        ResponseFundHistory responseFundHistory = service.getHistory(fundCode, fromDate, toDate);

        if (responseFundHistory == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(responseFundHistory);
    }

    @GetMapping("/{fundCode}/returns")
    public ResponseEntity<ResponseFundReturns> getReturns(
            @PathVariable String fundCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterDate
    ) {
        ResponseFundReturns responseFundReturns = service.getReturns(fundCode, beforeDate, afterDate);

        if (responseFundReturns == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(responseFundReturns);
    }
}
