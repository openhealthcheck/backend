package com.wirvsvirus.backend.service;

import com.wirvsvirus.backend.exception.InvalidInputException;
import com.wirvsvirus.backend.exception.QuestionNotFoundException;
import com.wirvsvirus.backend.exception.QuestionnaireCompletedException;
import com.wirvsvirus.backend.model.Question;
import com.wirvsvirus.backend.model.Questionnaire;
import com.wirvsvirus.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    QuestionRepository questionRepository;
    UserService userService;
    QuestionnaireService questionnaireService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           UserService userService,
                           QuestionnaireService questionnaireService) {
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.questionnaireService = questionnaireService;
    }

    public List<Question> listQuestions(Long userId, Long questionnaireId) {
        userService.verifyUserExists(userId);
        questionnaireService.verifyQuestionnaireExists(questionnaireId);
        return questionRepository.listQuestionnairesByUserId(userId, questionnaireId);
    }

    public Question getQuestion(Long userId, Long questionnaireId, Long questionId) {
        userService.verifyUserExists(userId);
        questionnaireService.verifyQuestionnaireExists(questionnaireId);

        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isEmpty()) {
            throw new QuestionNotFoundException("The question having the 'questionId': '" +
                    question + "' does not exist");
        }
        return question.get();
    }

    public Question updateQuestion(Long userId, Long questionnaireId, Long questionId, Question question) {
        userService.verifyUserExists(userId);
        questionnaireService.verifyQuestionnaireExists(questionnaireId);
        Question existingQuestion = getQuestion(userId, questionnaireId, questionId);

        // Verify that questionaire is not completed
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(userId, questionnaireId);
        if (questionnaire.isCompleted()) {
            throw new QuestionnaireCompletedException("Questionaire with id: " + questionnaireId + " is already completed.");
        }

        // Verify that the question value is valid
        if (question.getValue() == null) {
            throw new InvalidInputException("The question value needs to be set");
        }
        switch (existingQuestion.getType().toLowerCase()) {
            case "atomic":
                existingQuestion.getOptions().stream()
                        .filter(option -> option.getName().toLowerCase().equals(question.getValue().toLowerCase()))
                        .findFirst()
                        .orElseThrow(() -> new InvalidInputException("Question value does not match any of the atomic options"));
                break;
            case "double":
                try {
                    Double.parseDouble(question.getValue());
                } catch (NumberFormatException nfe) {
                    throw new InvalidInputException("Question is not convertible to a double type");
                }
                break;
            case "int":
                try {
                    Integer.parseInt(question.getValue());
                } catch (NumberFormatException nfe) {
                    throw new InvalidInputException("Question is not convertible to a double type");
                }
                break;
            // string question types are currently not validated
        }

        existingQuestion.setValue(question.getValue());

        return questionRepository.save(existingQuestion);
    }

    public void completeQuestionaire(Long userId, Long questionaireId) {
        questionnaireService.completeQuestionnaire(userId, questionaireId);
    }


}
