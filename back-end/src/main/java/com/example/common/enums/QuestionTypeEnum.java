package com.example.common.enums;

public enum QuestionTypeEnum {

    SINGLE("Single Choice"),
    MULTIPLE("多选题"),
    TEXT("填空题");

    private String value;

    public String getValue() {
        return value;
    }

    QuestionTypeEnum(String value) {
        this.value = value;
    }
}