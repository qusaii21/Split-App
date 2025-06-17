package com.DevDynamics.splitapp.service;

import com.DevDynamics.splitapp.dto.ExpenseDTO;
import com.DevDynamics.splitapp.dto.ParticipantShare;
import com.DevDynamics.splitapp.enums.ShareType;
import com.DevDynamics.splitapp.exception.ResourceNotFoundException;
import com.DevDynamics.splitapp.exception.ValidationException;
import com.DevDynamics.splitapp.model.Expense;
import com.DevDynamics.splitapp.model.Category;
import com.DevDynamics.splitapp.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for Expense operations
 */
@Service
public class ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    @Autowired
    private ExpenseRepository expenseRepository;

    /**
     * Get all expenses
     */
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    /**
     * Get expense by ID
     */
    public Expense getExpenseById(String id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
    }

    /**
     * Get expenses by category
     */
    public List<Expense> getExpensesByCategory(Category category) {
        return expenseRepository.findByCategory(category);
    }

    /**
     * Create a new expense
     */
    public Expense createExpense(ExpenseDTO expenseDTO) {
        validateExpenseDTO(expenseDTO);

        Expense expense = new Expense();
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setPaidBy(expenseDTO.getPaidBy());
        expense.setShareType(expenseDTO.getShareType());
        expense.setCategory(expenseDTO.getCategory());
        expense.setDate(expenseDTO.getDate());

        List<ParticipantShare> participants = processParticipants(expenseDTO);
        expense.setParticipants(participants);

        Expense savedExpense = expenseRepository.save(expense);
        logger.info("Created expense with id: {}", savedExpense.getId());
        return savedExpense;
    }

    /**
     * Update an existing expense
     */
    public Expense updateExpense(String id, ExpenseDTO expenseDTO) {
        validateExpenseDTO(expenseDTO);

        Expense existingExpense = getExpenseById(id);
        existingExpense.setDescription(expenseDTO.getDescription());
        existingExpense.setAmount(expenseDTO.getAmount());
        existingExpense.setPaidBy(expenseDTO.getPaidBy());
        existingExpense.setShareType(expenseDTO.getShareType());
        existingExpense.setCategory(expenseDTO.getCategory());
        existingExpense.setDate(expenseDTO.getDate());

        List<ParticipantShare> participants = processParticipants(expenseDTO);
        existingExpense.setParticipants(participants);

        Expense updatedExpense = expenseRepository.save(existingExpense);
        logger.info("Updated expense with id: {}", updatedExpense.getId());
        return updatedExpense;
    }

    /**
     * Delete an expense
     */
    public void deleteExpense(String id) {
        Expense expense = getExpenseById(id);
        expenseRepository.delete(expense);
        logger.info("Deleted expense with id: {}", id);
    }

    /**
     * Get all unique people from expenses
     */
    public Set<String> getAllPeople() {
        Set<String> people = new HashSet<>();
        // fetch once
        List<Expense> allExpenses = expenseRepository.findAll();

        for (Expense e : allExpenses) {
            // the payer
            people.add(e.getPaidBy());
            // any participants
            if (e.getParticipants() != null) {
                for (ParticipantShare p : e.getParticipants()) {
                    people.add(p.getName());
                }
            }
        }
        return people;
    }

   

    

/**
 * Monthly spending summary by category
 */
public Map<String, Double> getMonthlySpendingSummary(int year, int month) {
    YearMonth target = YearMonth.of(year, month);
    return expenseRepository.findAll().stream()
        .filter(e -> e.getDate() != null                              
                  && YearMonth.from(e.getDate()).equals(target)
                  && e.getCategory() != null)                         
        .collect(Collectors.groupingBy(
            e -> e.getCategory().name(),
            Collectors.summingDouble(Expense::getAmount)
        ));
}

/**
 * Top N categories by total spending
 */
public List<String> getTopNCategories(int n) {
    Map<String, Double> summary = expenseRepository.findAll().stream()
        .filter(e -> e.getCategory() != null)                         
        .collect(Collectors.groupingBy(
            e -> e.getCategory().name(),
            Collectors.summingDouble(Expense::getAmount)
        ));

    return summary.entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .limit(n)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
}

   

 


    private void validateExpenseDTO(ExpenseDTO dto) {
        if (dto.getAmount() == null || dto.getAmount() <= 0)
            throw new ValidationException("Amount must be greater than 0");
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty())
            throw new ValidationException("Description is required");
        if (dto.getPaidBy() == null || dto.getPaidBy().trim().isEmpty())
            throw new ValidationException("Paid by is required");
        if (dto.getShareType() == null)
            dto.setShareType(ShareType.EQUAL);
        if (dto.getCategory() == null)
            throw new ValidationException("Category is required");
        if (dto.getDate() == null)
            throw new ValidationException("Date is required");
    }

    private List<ParticipantShare> processParticipants(ExpenseDTO dto) {
        List<ParticipantShare> participants = dto.getParticipants();
        if (participants == null || participants.isEmpty()) {
            participants = Collections.singletonList(
                    new ParticipantShare(dto.getPaidBy(), null));
        }
        boolean includesPayer = participants.stream()
                .anyMatch(p -> p.getName().equals(dto.getPaidBy()));
        if (!includesPayer) {
            participants = new ArrayList<>(participants);
            participants.add(new ParticipantShare(dto.getPaidBy(), null));
        }
        return calculateShares(participants, dto.getAmount(), dto.getShareType());
    }

    

    private List<ParticipantShare> calculateShares(List<ParticipantShare> participants,
            Double totalAmount,
            ShareType type) {
        List<ParticipantShare> result = new ArrayList<>();
        switch (type) {
            case EQUAL:
            
                final double equalShare = round(totalAmount / participants.size());
                participants.forEach(p -> result.add(new ParticipantShare(p.getName(), equalShare)));
                break;

            case PERCENTAGE:
                validatePercentageShares(participants);
                participants.forEach(p -> {
                    double amt = (totalAmount * p.getShareValue()) / 100.0;
                    result.add(new ParticipantShare(p.getName(), round(amt)));
                });
                break;

            case EXACT:
                validateExactShares(participants, totalAmount);
                result.addAll(participants);
                break;
        }
        return result;
    }

    private void validatePercentageShares(List<ParticipantShare> parts) {
        double sum = parts.stream()
                .mapToDouble(p -> Optional.ofNullable(p.getShareValue()).orElse(0.0))
                .sum();
        if (Math.abs(sum - 100.0) > 0.01)
            throw new ValidationException("Percentage shares must sum to 100%");
    }

    private void validateExactShares(List<ParticipantShare> parts, Double total) {
        double sum = parts.stream()
                .mapToDouble(p -> Optional.ofNullable(p.getShareValue()).orElse(0.0))
                .sum();
        if (Math.abs(sum - total) > 0.01)
            throw new ValidationException("Exact shares must sum to total amount");
    }

    
    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
