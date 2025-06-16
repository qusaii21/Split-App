package com.DevDynamics.splitapp.controller;

import com.DevDynamics.splitapp.dto.*;
import com.DevDynamics.splitapp.model.Expense;
import com.DevDynamics.splitapp.model.Category;          
import com.DevDynamics.splitapp.service.ExpenseService;
import com.DevDynamics.splitapp.service.SettlementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * REST Controller for expense management
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ExpenseController {
    
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    
    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private SettlementService settlementService;
    
    /**
     * Get all expenses
     */
    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<List<Expense>>> getAllExpenses() {
        logger.info("Getting all expenses");
        List<Expense> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(ApiResponse.success(expenses, "Expenses retrieved successfully"));
    }
    
    /**
     * Get expense by ID
     */
    @GetMapping("/expenses/{id}")
    public ResponseEntity<ApiResponse<Expense>> getExpenseById(@PathVariable String id) {
        logger.info("Getting expense with id: {}", id);
        Expense expense = expenseService.getExpenseById(id);
        return ResponseEntity.ok(ApiResponse.success(expense, "Expense retrieved successfully"));
    }
    
    /**
     * Get expenses by category
     */
    @GetMapping("/expenses/category/{category}")
    public ResponseEntity<ApiResponse<List<Expense>>> getExpensesByCategory(@PathVariable Category category) {
        logger.info("Getting expenses in category: {}", category);
        List<Expense> expenses = expenseService.getExpensesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(expenses, "Expenses by category retrieved successfully"));
    }
    
    /**
     * Create new expense
     */
    @PostMapping("/expenses")
    public ResponseEntity<ApiResponse<Expense>> createExpense(@Valid @RequestBody ExpenseDTO expenseDTO) {
        logger.info("Creating new expense: {}", expenseDTO.getDescription());
        Expense expense = expenseService.createExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(expense, "Expense created successfully"));
    }
    
    /**
     * Update existing expense
     */
    @PutMapping("/expenses/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpense(
            @PathVariable String id, 
            @Valid @RequestBody ExpenseDTO expenseDTO) {
        logger.info("Updating expense with id: {}", id);
        Expense expense = expenseService.updateExpense(id, expenseDTO);
        return ResponseEntity.ok(ApiResponse.success(expense, "Expense updated successfully"));
    }
    
    /**
     * Delete expense
     */
    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteExpense(@PathVariable String id) {
        logger.info("Deleting expense with id: {}", id);
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Expense deleted successfully"));
    }
    
    /**
     * Get all people
     */
    @GetMapping("/people")
    public ResponseEntity<ApiResponse<Set<String>>> getAllPeople() {
        logger.info("Getting all people");
        Set<String> people = expenseService.getAllPeople();
        return ResponseEntity.ok(ApiResponse.success(people, "People retrieved successfully"));
    }
    
    /**
     * Get current balances
     */
    @GetMapping("/balances")
    public ResponseEntity<ApiResponse<List<BalanceDTO>>> getBalances() {
        logger.info("Getting current balances");
        List<BalanceDTO> balances = settlementService.calculateBalances();
        return ResponseEntity.ok(ApiResponse.success(balances, "Balances calculated successfully"));
    }
    
    /**
     * Get settlement summary
     */
    @GetMapping("/settlements")
    public ResponseEntity<ApiResponse<List<SettlementDTO>>> getSettlements() {
        logger.info("Getting settlement summary");
        List<SettlementDTO> settlements = settlementService.calculateSettlements();
        return ResponseEntity.ok(ApiResponse.success(settlements, "Settlements calculated successfully"));
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.success("OK", "Service is healthy"));
    }
}
