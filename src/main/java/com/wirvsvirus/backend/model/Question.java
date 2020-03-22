package com.wirvsvirus.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question extends RepresentationModel<Question> {
    private Long userId;
    private Long questionnaireId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    // id in json
    private Integer questionNumber;

    @Column(columnDefinition="TEXT")
    private String question;
    @Column(columnDefinition="TEXT")
    private String description;

    // Depending on "type" this needs to be cast when it is processed
    private String value;
    private String type;
    private String category;

    @ElementCollection
    private List<Option> options;
}
