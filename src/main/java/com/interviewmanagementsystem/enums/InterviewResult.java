package com.interviewmanagementsystem.enums;

public enum InterviewResult {
    PASSED("Passed"),
    FAILED("Failed"),
    NA("N/A");


    private final String displayValue;

    InterviewResult(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}