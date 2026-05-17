package com.example.prs.model.enums;

public enum OrderStatus {
    CREATED("Создан"),
    INREPAIR("Ремонт в процессе"),
    READY("Готово к выдаче"),
    COMPLETED("Завершен"),
    CANCELED("Отменен");

    private final String displayName;

    OrderStatus(String displayName){
        this.displayName=displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}

