package com.DevDynamics.splitapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;


public class ParticipantShare {
    
    @NotBlank(message = "Participant name cannot be blank")
    private String name;
    
    @PositiveOrZero(message = "Share value must be positive or zero")
    private Double shareValue;     
    public ParticipantShare() {}
    
    public ParticipantShare(String name, Double shareValue) {
        this.name = name;
        this.shareValue = shareValue;
    }
    
   
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getShareValue() {
        return shareValue;
    }
    
    public void setShareValue(Double shareValue) {
        this.shareValue = shareValue;
    }
}
