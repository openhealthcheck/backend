package com.wirvsvirus.backend.model.parsed;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class QuestionnaireJson {
    private String version;
    private long startQuestionId;
    private List<QuestionJson> questions;

    @JsonGetter("version")
    public String getVersion() {
        return version;
    }

    @JsonSetter("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonGetter("start")
    public long getStartQuestionId() {
        return startQuestionId;
    }

    @JsonSetter("start")
    public void setStartQuestionId(long startQuestionId) {
        this.startQuestionId = startQuestionId;
    }

    @JsonGetter("questions")
    public List<QuestionJson> getQuestions() {
        return questions;
    }

    @JsonSetter("questions")
    public void setQuestions(List<QuestionJson> questions) {
        this.questions = questions;
    }

}


