package com.wirvsvirus.backend.repository;

import com.wirvsvirus.backend.model.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.userId = :user_id AND q.questionnaireId = :questionnaire_id")
    List<Question> listQuestionnairesByUserId(@Param("user_id") long userId,
                                              @Param("questionnaire_id") long questionnaireId);
}
