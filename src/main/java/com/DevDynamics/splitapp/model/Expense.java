package com.DevDynamics.splitapp.model;



import com.DevDynamics.splitapp.dto.ParticipantShare;
import com.DevDynamics.splitapp.enums.ShareType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "expenses")
public class Expense {
    
    @Id
    private String id;
    
    private Double amount;
    
    private String description;
    
    private String paidBy;
    
    private ShareType shareType;
    
    private List<ParticipantShare> participants;

    private Category category;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDate date; 
    
    public Expense() {}
    
    public Expense(Double amount, String description, String paidBy, ShareType shareType, List<ParticipantShare> participants) {
        this.amount = amount;
        this.description = description;
        this.paidBy = paidBy;
        this.shareType = shareType;
        this.participants = participants;
    }
    
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
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
    
    public void setParticipants(List<ParticipantShare> participants2) {
        this.participants = participants2;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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