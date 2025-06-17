package com.DevDynamics.splitapp.dto;


public class BalanceDTO {
    
    private String person;
    private Double netBalance; 
    private Double totalPaid;
    private Double totalOwed;
    
    public BalanceDTO() {}
    
    public BalanceDTO(String person, Double netBalance, Double totalPaid, Double totalOwed) {
        this.person = person;
        this.netBalance = netBalance;
        this.totalPaid = totalPaid;
        this.totalOwed = totalOwed;
    }
    
    
    public String getPerson() {
        return person;
    }
    
    public void setPerson(String person) {
        this.person = person;
    }
    
    public Double getNetBalance() {
        return netBalance;
    }
    
    public void setNetBalance(Double netBalance) {
        this.netBalance = netBalance;
    }
    
    public Double getTotalPaid() {
        return totalPaid;
    }
    
    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }
    
    public Double getTotalOwed() {
        return totalOwed;
    }
    
    public void setTotalOwed(Double totalOwed) {
        this.totalOwed = totalOwed;
    }
}
