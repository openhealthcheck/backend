package com.wirvsvirus.backend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
public class Questionaires extends RepresentationModel<Questionaires> {
    private List<Questionnaire> questionnaires;
    private Long userId;
}
