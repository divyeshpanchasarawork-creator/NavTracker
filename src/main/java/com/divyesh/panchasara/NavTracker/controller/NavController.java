package com.divyesh.panchasara.NavTracker.controller;

import com.divyesh.panchasara.NavTracker.dto.ResponseFund;
import com.divyesh.panchasara.NavTracker.dto.ResponseFundHistory;
import com.divyesh.panchasara.NavTracker.dto.ResponseFundReturns;
import com.divyesh.panchasara.NavTracker.service.NavService;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/funds")
@Validated
public class NavController {

    private final NavService service;

    public NavController(NavService service) {
        this.service = service;
    }

    @GetMapping("/{fundCode}/latest-nav")
    public ResponseEntity<ResponseFund> getLatestNav(
            @PathVariable
            @Pattern(regexp = "^(?i)[A-Z0-9]{12}$", message = "Invalid fund code format")
            String fundCode
    ) {
        return ResponseEntity.ok(service.getLatest(fundCode));
    }

    @GetMapping("/{fundCode}/history")
    public ResponseEntity<ResponseFundHistory> getHistory(
            @PathVariable
            @Pattern(regexp = "^(?i)[A-Z0-9]{12}$", message = "Invalid fund code format")
            String fundCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return ResponseEntity.ok(service.getHistory(fundCode, fromDate, toDate));
    }

    @GetMapping("/{fundCode}/returns")
    public ResponseEntity<ResponseFundReturns> getReturns(
            @PathVariable
            @Pattern(regexp = "^(?i)[A-Z0-9]{12}$", message = "Invalid fund code format")
            String fundCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate afterDate
    ) {
        return ResponseEntity.ok(service.getReturns(fundCode, beforeDate, afterDate));
    }
}