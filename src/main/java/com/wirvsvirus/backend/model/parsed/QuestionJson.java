package com.wirvsvirus.backend.model.parsed;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class QuestionJson {

    private long questionId;
    private String question;
    private String description;
    private String type;
    private String category;
    private List<OptionJson> optionJsons;

    @JsonGetter("id")
    public long getQuestionId() {
        return questionId;
    }

    @JsonSetter("id")
    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @JsonGetter("question")
    public String getQuestion() {
        return question;
    }

    @JsonSetter("question")
    public void setQuestion(String question) {
        this.question = question;
    }

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonGetter("type")
    public String getType() {
        return type;
    }

    @JsonSetter("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonGetter("category")
    public String getCategory() {
        return category;
    }

    @JsonSetter("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonGetter("options")
    public List<OptionJson> getOptionJsons() {
        return optionJsons;
    }

    @JsonSetter("options")
    public void setOptionJsons(List<OptionJson> optionJsons) {
        this.optionJsons = optionJsons;
    }
}
