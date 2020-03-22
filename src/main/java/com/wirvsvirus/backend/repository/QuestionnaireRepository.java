package com.wirvsvirus.backend.repository;

import com.wirvsvirus.backend.model.Questionnaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionnaireRepository extends CrudRepository<Questionnaire, Long> {

    @Query("SELECT q FROM Questionnaire q WHERE q.userId = :user_id")
    List<Questionnaire> listQuestionnairesByUserId(@Param("user_id") long userId);
}
