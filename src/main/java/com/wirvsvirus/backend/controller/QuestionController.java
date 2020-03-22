package com.wirvsvirus.backend.controller;

import com.wirvsvirus.backend.exception.InternalServerException;
import com.wirvsvirus.backend.model.Option;
import com.wirvsvirus.backend.model.Question;
import com.wirvsvirus.backend.model.Questions;
import com.wirvsvirus.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users/{userId}/questionnaires/{questionnaireId}/questions")
public class QuestionController {

    QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Questions getQuestions(@PathVariable("userId") Long userId,
                                  @PathVariable("questionnaireId") Long questionnaireId) {

        Questions questions = Questions.builder()
                .questions(questionService.listQuestions(userId, questionnaireId))
                .questionnaireId(questionnaireId)
                .userId(userId)
                .build();

        questions.add(linkTo(methodOn(QuestionController.class).getQuestions(userId, questionnaireId)).withSelfRel());
        questions.add(linkTo(methodOn(UserController.class).getUser(userId)).withRel("user"));
        questions.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaire(userId, questionnaireId)).withRel("questionnaire"));
        return questions;
    }

    @RequestMapping(value = "/{questionId}", method = RequestMethod.GET)
    public Question getQuestion(@PathVariable("userId") Long userId,
                                @PathVariable("questionnaireId") Long questionnaireId,
                                @PathVariable("questionId") Long questionId) {

        Question question = questionService.getQuestion(userId, questionnaireId, questionId);


        question.add(linkTo(methodOn(QuestionController.class).getQuestion(userId, questionnaireId, questionId)).withSelfRel());
        question.add(linkTo(methodOn(UserController.class).getUser(userId)).withRel("user"));
        question.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaire(userId, questionnaireId)).withRel("questionnaire"));
        question.add(linkTo(methodOn(QuestionController.class).getQuestions(userId, questionnaireId)).withRel("all"));
        question.add(linkTo(methodOn(QuestionController.class).updateQuestion(userId, questionnaireId, questionId, question)).withRel("update_question"));
        addNextQuestionLink(question, false);

        return question;
    }

    @RequestMapping(value = "/{questionId}", method = RequestMethod.PUT)
    public ResponseEntity<Question> updateQuestion(@PathVariable("userId") Long userId,
                                                   @PathVariable("questionnaireId") Long questionnaireId,
                                                   @PathVariable("questionId") Long questionId,
                                                   @RequestBody Question question) {

        Question updatedQuestion = questionService.updateQuestion(userId, questionnaireId, questionId, question);

        updatedQuestion.add(linkTo(methodOn(QuestionController.class).getQuestion(userId, questionnaireId, questionId)).withSelfRel());
        updatedQuestion.add(linkTo(methodOn(UserController.class).getUser(userId)).withRel("user"));
        updatedQuestion.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaire(userId, questionnaireId)).withRel("questionnaire"));
        updatedQuestion.add(linkTo(methodOn(QuestionController.class).getQuestions(userId, questionnaireId)).withRel("all"));
        updatedQuestion.add(linkTo(methodOn(QuestionController.class).updateQuestion(userId, questionnaireId, questionId, question)).withRel("update_question"));
        addNextQuestionLink(updatedQuestion, true);

        return ResponseEntity.ok(updatedQuestion);
    }

    private void addNextQuestionLink(Question question, boolean isUpdate) {
        /*
         * if value == null -> no link
         * if type == atomic -> match value on name in options. get id of question number in next and return. Ex. -1 questionaire is finished link to questionaire/summery should be displayed instead
         * if type != atomic -> take first option object and proceed as above
         */
        if (question.getValue() == null) {
            return;
        }

        List<Question> questions = questionService.listQuestions(question.getUserId(), question.getQuestionnaireId());

        Integer nextQuestionNumber;

        if (question.getType().equals("atomic")) {
            nextQuestionNumber = question.getOptions().stream()
                    .filter(option -> option.getName().equals(question.getValue()))
                    .map(Option::getNext)
                    .findFirst()
                    .orElse(null);
            if (nextQuestionNumber == null) {
                throw new InternalServerException("Could not find matching option for question value " + question.getValue());
            }

        } else {
            if (question.getOptions().size() != 1) {
                throw new InternalServerException("Option array for non-atomic question does not have the length of one");
            }
            nextQuestionNumber = question.getOptions().get(0).getNext();
        }

        // if next question is -1 then link to summary should be displayed
        if (nextQuestionNumber.equals(-1)) {
            // if this was called from the update method, also complete the questionnaire. Sorry for the bad side effect
            questionService.completeQuestionaire(question.getUserId(), question.getQuestionnaireId());

            question.add(linkTo(methodOn(QuestionnaireController.class).getQuestionnaireSummary(question.getUserId(), question.getQuestionnaireId())).withRel("next"));
            return;
        }
        Question nextQuestion = findNextQuestion(questions, nextQuestionNumber);
        question.add(linkTo(methodOn(QuestionController.class).getQuestion(question.getUserId(), question.getQuestionnaireId(), nextQuestion.getQuestionId())).withRel("next"));
    }

    private Question findNextQuestion(List<Question> questionList, Integer nextQuestionNumber) {
        Question nextQuestion = questionList.stream()
                .filter(questionTest -> questionTest.getQuestionNumber().equals(nextQuestionNumber))
                .findFirst()
                .orElse(null);
        if (nextQuestion == null) {
            throw new InternalServerException("Could not find next question with number " + nextQuestionNumber);
        }
        return nextQuestion;
    }


}
