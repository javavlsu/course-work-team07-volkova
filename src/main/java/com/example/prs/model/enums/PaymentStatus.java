package com.example.prs.model.enums;

public enum PaymentStatus {
    WAITING("Ожидание"),
    SUCCESS("Успешно"),
    ERROR("Ошибка");

    private final String displayName;

    PaymentStatus(String displayName){
        this.displayName=displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
