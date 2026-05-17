package com.example.prs.model.enums;

public enum UserRole {
    ADMIN("Администратор"),
    EMPLOYEE("Сотрудник"),
    CLIENT("Клиент");

    private final String displayName;

    UserRole(String displayName){
        this.displayName=displayName;
    }
    
    public String getDisplayName(){
        return displayName;
    }
}
