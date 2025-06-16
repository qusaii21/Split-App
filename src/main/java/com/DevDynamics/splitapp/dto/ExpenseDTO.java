package com.DevDynamics.splitapp.dto;
import com.DevDynamics.splitapp.enums.ShareType;
import com.DevDynamics.splitapp.model.Category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;


public class ExpenseDTO {
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private Double amount;
    
    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
    
    @NotBlank(message = "Paid by is required")
    private String paidBy;
    
    @NotNull(message = "Share type is required")
    private ShareType shareType = ShareType.EQUAL;
    
    @Valid
    private List<ParticipantShare> participants;

    private Category category;

    private LocalDate date; 
    
    public ExpenseDTO() {}
    
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPaidBy() {
        return paidBy;
    }
    
    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }
    
    public ShareType getShareType() {
        return shareType;
    }
    
    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }
    
    public List<ParticipantShare> getParticipants() {
        return participants;
    }
    
    public void setParticipants(List<ParticipantShare> participants) {
        this.participants = participants;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

}
