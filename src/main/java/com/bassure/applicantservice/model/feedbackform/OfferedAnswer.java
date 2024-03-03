package com.bassure.applicantservice.model.feedbackform;

public enum OfferedAnswer {
    FIVE("FIVE"),
    FOUR("FOUR"),
    THREE("THREE"),
    TWO("TWO"),
    ONE("ONE");

    private String value;

    private OfferedAnswer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
