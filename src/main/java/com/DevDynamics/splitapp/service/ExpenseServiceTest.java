// package com.DevDynamics.splitapp.service;

// import com.DevDynamics.splitapp.dto.ExpenseDTO;
// import com.DevDynamics.splitapp.dto.ParticipantShare;
// import com.DevDynamics.splitapp.enums.ShareType;
// import com.DevDynamics.splitapp.exception.ResourceNotFoundException;
// import com.DevDynamics.splitapp.exception.ValidationException;
// import com.DevDynamics.splitapp.model.Expense;
// import com.DevDynamics.splitapp.repository.ExpenseRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class ExpenseServiceTest {
    
//     @Mock
//     private ExpenseRepository expenseRepository;
    
//     @InjectMocks
//     private ExpenseService expenseService;
    
//     private ExpenseDTO validExpenseDTO;
//     private Expense mockExpense;
    
//     @BeforeEach
//     void setUp() {
//         validExpenseDTO = new ExpenseDTO();
//         validExpenseDTO.setAmount(100.0);
//         validExpenseDTO.setDescription("Test expense");
//         validExpenseDTO.setPaidBy("John");
//         validExpenseDTO.setShareType(ShareType.EQUAL);
//         validExpenseDTO.setParticipants(Arrays.asList(
//             new ParticipantShare("John", null),
//             new ParticipantShare("Jane", null)
//         ));
        
//         mockExpense = new Expense();
//         mockExpense.setId("1");
//         mockExpense.setAmount(100.0);
//         mockExpense.setDescription("Test expense");
//         mockExpense.setPaidBy("John");
//         mockExpense.setShareType(ShareType.EQUAL);
//     }
    
//     @Test
//     void getAllExpenses_ShouldReturnAllExpenses() {
//         // Given
//         List<Expense> expectedExpenses = Arrays.asList(mockExpense);
//         when(expenseRepository.findAll()).thenReturn(expectedExpenses);
        
//         // When
//         List<Expense> result = expenseService.getAllExpenses();
        
//         // Then
//         assertEquals(expectedExpenses, result);
//         verify(expenseRepository).findAll();
//     }
    
//     @Test
//     void getExpenseById_WithValidId_ShouldReturnExpense() {
//         // Given
//         when(expenseRepository.findById("1")).thenReturn(Optional.of(mockExpense));
        
//         // When
//         Expense result = expenseService.getExpenseById("1");
        
//         // Then
//         assertEquals(mockExpense, result);
//         verify(expenseRepository).findById("1");
//     }
    
//     @Test
//     void getExpenseById_WithInvalidId_ShouldThrowException() {
//         // Given
//         when(expenseRepository.findById("invalid")).thenReturn(Optional.empty());
        
//         // When & Then
//         assertThrows(ResourceNotFoundException.class, 
//             () -> expenseService.getExpenseById("invalid"));
//         verify(expenseRepository).findById("invalid");
//     }
    
//     @Test
//     void createExpense_WithValidData_ShouldCreateExpense() {
//         // Given
//         when(expenseRepository.save(any(Expense.class))).thenReturn(mockExpense);
        
//         // When
//         Expense result = expenseService.createExpense(validExpenseDTO);
        
//         // Then
//         assertNotNull(result);
//         verify(expenseRepository).save(any(Expense.class));
//     }
    
//     @Test
//     void createExpense_WithInvalidAmount_ShouldThrowException() {
//         // Given
//         validExpenseDTO.setAmount(-10.0);
        
//         // When & Then
//         assertThrows(ValidationException.class, 
//             () -> expenseService.createExpense(validExpenseDTO));
//         verify(expenseRepository, never()).save(any(Expense.class));
//     }
    
//     @Test
//     void createExpense_WithEmptyDescription_ShouldThrowException() {
//         // Given
//         validExpenseDTO.setDescription("");
        
//         // When & Then
//         assertThrows(ValidationException.class, 
//             () -> expenseService.createExpense(validExpenseDTO));
//         verify(expenseRepository, never()).save(any(Expense.class));
//     }
    
//     @Test
//     void createExpense_WithEmptyPaidBy_ShouldThrowException() {
//         // Given
//         validExpenseDTO.setPaidBy("");
        
//         // When & Then
//         assertThrows(ValidationException.class, 
//             () -> expenseService.createExpense(validExpenseDTO));
//         verify(expenseRepository, never()).save(any(Expense.class));
//     }
    
//     @Test
//     void updateExpense_WithValidData_ShouldUpdateExpense() {
//         // Given
//         when(expenseRepository.findById("1")).thenReturn(Optional.of(mockExpense));
//         when(expenseRepository.save(any(Expense.class))).thenReturn(mockExpense);
        
//         // When
//         Expense result = expenseService.updateExpense("1", validExpenseDTO);
        
//         // Then
//         assertNotNull(result);
//         verify(expenseRepository).findById("1");
//         verify(expenseRepository).save(any(Expense.class));
//     }
    
//     @Test
//     void deleteExpense_WithValidId_ShouldDeleteExpense() {
//         // Given
//         when(expenseRepository.findById("1")).thenReturn(Optional.of(mockExpense));
        
//         // When
//         expenseService.deleteExpense("1");
        
//         // Then
//         verify(expenseRepository).findById("1");
//         verify(expenseRepository).delete(mockExpense);
//     }
    
//     @Test
//     void getAllPeople_ShouldReturnUniqueNames() {
//         // Given
//         List<Expense> expenses = Arrays.asList(mockExpense);
//         mockExpense.setParticipants(Arrays.asList(
//             new ParticipantShare("John", 50.0),
//             new ParticipantShare("Jane", 50.0)
//         ));
//         when(expenseRepository.findAll()).thenReturn(expenses);
        
//         // When
//         Set<String> result = expenseService.getAllPeople();
        
//         // Then
//         assertTrue(result.contains("John"));
//         assertTrue(result.contains("Jane"));
//         verify(expenseRepository).findAll();
//     }
// }