package com.pira.piraproject.enums;

public enum Category {
    WORK("WORK"),
    HEALTH("HEALTH"),
    FINANCE("FINANCE"),
    PERSONAL("PERSONAL");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}