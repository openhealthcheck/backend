package com.wirvsvirus.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireSummary extends RepresentationModel<QuestionnaireSummary>{
    private Long userId;
    private Long questionnaireId;

    @JsonProperty("degree_of_sickness")
    private Double degreeOfSickness;
}
