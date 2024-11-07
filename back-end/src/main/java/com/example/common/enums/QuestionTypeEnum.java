package com.example.common.enums;

public enum QuestionTypeEnum {

    SINGLE("Single Choice"),
    MULTIPLE("Multiple Choice"),
    TEXT("Blank");

    private String value;

    public String getValue() {
        return value;
    }

    QuestionTypeEnum(String value) {
        this.value = value;
    }
}