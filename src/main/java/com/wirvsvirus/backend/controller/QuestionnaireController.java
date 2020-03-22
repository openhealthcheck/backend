package com.wirvsvirus.backend.controller;

import com.wirvsvirus.backend.exception.InternalServerException;
import com.wirvsvirus.backend.model.Question;
import com.wirvsvirus.backend.model.Questionaires;
import com.wirvsvirus.backend.model.Questionnaire;
import com.wirvsvirus.backend.model.QuestionnaireSummary;
import com.wirvsvirus.backend.service.QuestionService;
import com.wirvsvirus.backend.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users/{userId}/questionnaires")
public class QuestionnaireController {

    QuestionnaireService questionnaireService;
    QuestionService questionService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService,
                                   QuestionService questionService) {
        this.questionnaireService = questionnaireService;
        this.questionService = questionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Questionaires getQuestionnaires(@PathVariable("userId") Long userId) {
        Questionaires questionaires = Questionaires.builder()
                .questionnaires(questionnaireService.listQuestionnaires(userId))
                .userId(userId)
                .build();
        questionaires.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaires(userId)).withSelfRel());
        questionaires.add(linkTo(methodOn(UserController.class).getUser(userId)).withRel("user"));
        questionaires.add(linkTo(methodOn(QuestionnaireController.class).postQuestionaire(userId)).withRel("create_questionnaire"));
        return questionaires;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Questionnaire> postQuestionaire(@PathVariable("userId") Long userId) {
        Questionnaire questionnaire = questionnaireService.createQuestionnaire(userId);

        HttpHeaders responseHeaders = new HttpHeaders();
        String link = linkTo(methodOn(QuestionnaireController.class).getQuestionnaire(questionnaire.getUserId(),
                questionnaire.getQuestionnaireId())).toString();
        responseHeaders.set("location", link);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{questionnaireId}", method = RequestMethod.GET)
    public Questionnaire getQuestionnaire(@PathVariable("userId") Long userId,
                                          @PathVariable("questionnaireId") Long questionnaireId) {
        Questionnaire questionnaire = questionnaireService.getQuestionnaire(userId, questionnaireId);
        questionnaire.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaire(userId, questionnaireId)).withSelfRel());
        questionnaire.add(linkTo(methodOn(UserController.class).getUser(userId)).withRel("user"));
        questionnaire.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaires(userId)).withRel("all"));
        questionnaire.add(linkTo(methodOn(QuestionController.class).getQuestions(userId, questionnaireId)).withRel("questions"));
        if (questionnaire.isCompleted()) {
            questionnaire.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaireSummary(userId, questionnaireId)).withRel("summary"));
        } else {
            List<Question> questionList = questionService.listQuestions(userId, questionnaireId);
            Question firstQuestion = questionList.stream()
                    .filter(question -> question.getQuestionNumber().equals(1))
                    .findFirst()
                    .orElseThrow(() -> new InternalServerException("Could not find first question for questionnaire with id " + questionnaireId));
            questionnaire.add(linkTo(methodOn(QuestionController.class).getQuestion(userId, questionnaireId, firstQuestion.getQuestionId())).withRel("first"));
        }
        return questionnaire;
    }

    @RequestMapping(value = "/{questionnaireId}/summary", method = RequestMethod.GET)
    public QuestionnaireSummary getQuestionnaireSummary(@PathVariable("userId") Long userId,
                                                        @PathVariable("questionnaireId") Long questionnaireId) {
        QuestionnaireSummary questionnaireSummary = QuestionnaireSummary.builder()
                .degreeOfSickness(50.0)
                .build();

        questionnaireSummary.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaireSummary(userId, questionnaireId)).withSelfRel());
        questionnaireSummary.add(linkTo(methodOn(UserController.class).getUser(userId)).withRel("user"));
        questionnaireSummary.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaire(userId, questionnaireId)).withRel("questionnaire"));
        return questionnaireSummary;
    }
}
