package com.wirvsvirus.backend.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Questions extends RepresentationModel<Questions> {
    private List<Question> questions;
    private Long userId;
    private Long questionnaireId;
}
