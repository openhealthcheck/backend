package com.wirvsvirus.backend.model.parsed;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class OptionJson {
    private String name;
    private long nextQuestionId;
    private String color;

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter("next")
    public void setNextQuestionId(long nextQuestionId) {
        this.nextQuestionId = nextQuestionId;
    }

    @JsonSetter("color")
    public void setColor(String color) {
        this.color = color;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("next")
    public long getNextQuestionId() {
        return nextQuestionId;
    }

    @JsonGetter("color")
    public String getColor() {
        return color;
    }
}
