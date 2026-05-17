package com.example.prs.model.enums;

public enum PaymentMethod {
    ONLINE("Онлайн"),
    OFFLINE("Оффлайн");
    
    private final String displayName;

    PaymentMethod(String displayName){
        this.displayName=displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
