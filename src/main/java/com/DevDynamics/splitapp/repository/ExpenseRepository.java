package com.DevDynamics.splitapp.repository;



import com.DevDynamics.splitapp.model.Category;
import com.DevDynamics.splitapp.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    
    
    List<Expense> findByPaidBy(String paidBy);
    
    List<Expense> findByCategory(Category category);

    List<Expense> findByDescriptionContainingIgnoreCase(String description);
    
    
    List<Expense> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    
    @Query("{ $or: [ { 'paidBy': ?0 }, { 'participants.name': ?0 } ] }")
    List<Expense> findExpensesByPerson(String personName);
    
   
    @Query(value = "{}", fields = "{ 'paidBy': 1, '_id': 0 }")
    List<Expense> findDistinctPaidBy();
}