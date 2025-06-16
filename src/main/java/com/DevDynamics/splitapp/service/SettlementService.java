package com.DevDynamics.splitapp.service;

import com.DevDynamics.splitapp.dto.BalanceDTO;
import com.DevDynamics.splitapp.dto.ParticipantShare;
import com.DevDynamics.splitapp.dto.SettlementDTO;
import com.DevDynamics.splitapp.model.Expense;
import com.DevDynamics.splitapp.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for settlement calculations
 */
@Service
public class SettlementService {
    
    private static final Logger logger = LoggerFactory.getLogger(SettlementService.class);
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    /**
     * Calculate balances for all people
     */
    public List<BalanceDTO> calculateBalances() {
        List<Expense> expenses = expenseRepository.findAll();
        Map<String, BalanceCalculation> balanceMap = new HashMap<>();
        
        // Initialize balance calculations for all people
        Set<String> allPeople = getAllPeopleFromExpenses(expenses);
        for (String person : allPeople) {
            balanceMap.put(person, new BalanceCalculation());
        }
        
        // Process each expense
        for (Expense expense : expenses) {
            processExpenseForBalance(expense, balanceMap);
        }
        
        // Convert to BalanceDTO list
        List<BalanceDTO> balances = new ArrayList<>();
        for (Map.Entry<String, BalanceCalculation> entry : balanceMap.entrySet()) {
            String person = entry.getKey();
            BalanceCalculation calc = entry.getValue();
            
            double netBalance = calc.totalPaid - calc.totalOwed;
            netBalance = BigDecimal.valueOf(netBalance).setScale(2, RoundingMode.HALF_UP).doubleValue();
            
            balances.add(new BalanceDTO(
                person,
                netBalance,
                calc.totalPaid,
                calc.totalOwed
            ));
        }
        
        // Sort by net balance descending (people owed money first)
        balances.sort((a, b) -> Double.compare(b.getNetBalance(), a.getNetBalance()));
        
        logger.info("Calculated balances for {} people", balances.size());
        return balances;
    }
    
    /**
     * Calculate simplified settlements
     */
    public List<SettlementDTO> calculateSettlements() {
        List<BalanceDTO> balances = calculateBalances();
        
        // Separate creditors (positive balance) and debtors (negative balance)
        List<BalanceDTO> creditors = balances.stream()
                .filter(b -> b.getNetBalance() > 0.01)
                .collect(Collectors.toList());
        
        List<BalanceDTO> debtors = balances.stream()
                .filter(b -> b.getNetBalance() < -0.01)
                .collect(Collectors.toList());
        
        List<SettlementDTO> settlements = new ArrayList<>();
        
        // Use greedy approach to minimize number of transactions
        int creditorIndex = 0;
        int debtorIndex = 0;
        
        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {
            BalanceDTO creditor = creditors.get(creditorIndex);
            BalanceDTO debtor = debtors.get(debtorIndex);
            
            double creditAmount = creditor.getNetBalance();
            double debtAmount = Math.abs(debtor.getNetBalance());
            
            double settlementAmount = Math.min(creditAmount, debtAmount);
            settlementAmount = BigDecimal.valueOf(settlementAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
            
            if (settlementAmount > 0.01) {
                settlements.add(new SettlementDTO(
                    debtor.getPerson(),
                    creditor.getPerson(),
                    settlementAmount
                ));
                
                // Update balances
                creditor.setNetBalance(creditAmount - settlementAmount);
                debtor.setNetBalance(debtor.getNetBalance() + settlementAmount);
            }
            
            // Move to next creditor or debtor based on remaining balance
            if (Math.abs(creditor.getNetBalance()) < 0.01) {
                creditorIndex++;
            }
            if (Math.abs(debtor.getNetBalance()) < 0.01) {
                debtorIndex++;
            }
        }
        
        logger.info("Calculated {} settlements", settlements.size());
        return settlements;
    }
    
    /**
     * Get all unique people from expenses
     */
    private Set<String> getAllPeopleFromExpenses(List<Expense> expenses) {
        Set<String> people = new HashSet<>();
        
        for (Expense expense : expenses) {
            people.add(expense.getPaidBy());
            if (expense.getParticipants() != null) {
                people.addAll(expense.getParticipants().stream()
                        .map(ParticipantShare::getName)
                        .collect(Collectors.toSet()));
            }
        }
        
        return people;
    }
    
    /**
     * Process a single expense for balance calculation
     */
    private void processExpenseForBalance(Expense expense, Map<String, BalanceCalculation> balanceMap) {
        String paidBy = expense.getPaidBy();
        double amount = expense.getAmount();
        
        // Add to paid amount for the person who paid
        BalanceCalculation paidByBalance = balanceMap.get(paidBy);
        if (paidByBalance == null) {
            paidByBalance = new BalanceCalculation();
            balanceMap.put(paidBy, paidByBalance);
        }
        paidByBalance.totalPaid += amount;
        
        // Add to owed amount for each participant
        if (expense.getParticipants() != null) {
            for (ParticipantShare participant : expense.getParticipants()) {
                String participantName = participant.getName();
                double shareAmount = participant.getShareValue();
                
                BalanceCalculation participantBalance = balanceMap.get(participantName);
                if (participantBalance == null) {
                    participantBalance = new BalanceCalculation();
                    balanceMap.put(participantName, participantBalance);
                }
                participantBalance.totalOwed += shareAmount;
            }
        }
    }
    
    /**
     * Helper class for balance calculations
     */
    private static class BalanceCalculation {
        double totalPaid = 0.0;
        double totalOwed = 0.0;
    }
}