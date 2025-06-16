package com.DevDynamics.splitapp.controller;

import com.DevDynamics.splitapp.service.ExpenseService;
import com.DevDynamics.splitapp.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for analytics endpoints
 */
@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    @Autowired
    private ExpenseService expenseService;

    /**
     * Monthly spending summary by category
     * Example: GET /api/analytics/monthly-summary?year=2025&month=6
     */
    @GetMapping("/monthly-summary")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month
    ) {
        logger.info("Fetching monthly summary for {}/{}", month, year);
        Map<String, Double> summary = expenseService.getMonthlySpendingSummary(year, month);
        return ResponseEntity.ok(ApiResponse.success(summary, "Monthly summary retrieved successfully"));
    }

   

    /**
     * Top N categories by total spend
     * Example: GET /api/analytics/top-categories?n=3
     */
    @GetMapping("/top-categories")
    public ResponseEntity<ApiResponse<List<String>>> getTopCategories(
            @RequestParam(defaultValue = "3") int n
    ) {
        logger.info("Fetching top {} categories", n);
        List<String> topCats = expenseService.getTopNCategories(n);
        return ResponseEntity.ok(ApiResponse.success(topCats, "Top categories retrieved successfully"));
    }
}
