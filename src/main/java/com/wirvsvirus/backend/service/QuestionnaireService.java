package com.wirvsvirus.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wirvsvirus.backend.exception.InternalServerException;
import com.wirvsvirus.backend.exception.QuestionnaireNotFoundException;
import com.wirvsvirus.backend.exception.UserNotFoundException;
import com.wirvsvirus.backend.model.Option;
import com.wirvsvirus.backend.model.Question;
import com.wirvsvirus.backend.model.Questionnaire;
import com.wirvsvirus.backend.model.parsed.QuestionnaireJson;
import com.wirvsvirus.backend.repository.QuestionRepository;
import com.wirvsvirus.backend.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionnaireService {

    QuestionnaireRepository questionnaireRepository;
    UserService userService;
    QuestionRepository questionRepository;

    @Autowired
    public QuestionnaireService(QuestionnaireRepository questionnaireRepository,
                                UserService userService,
                                QuestionRepository questionRepository) {
        this.questionnaireRepository = questionnaireRepository;
        this.userService = userService;
        this.questionRepository= questionRepository;
    }

    public List<Questionnaire> listQuestionnaires(long userId) {
        userService.verifyUserExists(userId);
        return questionnaireRepository.listQuestionnairesByUserId(userId);
    }

    public Questionnaire createQuestionnaire(long userId) {
        userService.verifyUserExists(userId);
        QuestionnaireJson questionnaireJson = loadQuestionnaireFromFile("static/tree/v1.json");

        List<Question> questions = new ArrayList<>();
        questionnaireJson.getQuestions().forEach(questionJson -> {
            Question question = Question.builder()
                    .userId(userId)
                    .questionNumber((int) questionJson.getQuestionId())
                    .question(questionJson.getQuestion())
                    .description(questionJson.getDescription())
                    .type(questionJson.getType())
                    .category(questionJson.getCategory())
                    .build();
            List<Option> options = new ArrayList<>();
            questionJson.getOptionJsons().forEach(optionJson -> {
                Option option = new Option();
                option.setName(optionJson.getName());
                option.setNext((int) optionJson.getNextQuestionId());
                option.setColor(optionJson.getColor());
                options.add(option);
            });
            question.setOptions(options);
            questions.add(question);
        });

        Questionnaire questionnaire = Questionnaire.builder()
                .version(questionnaireJson.getVersion())
                .start(questionnaireJson.getStartQuestionId())
                .build();

        questionnaire.setQuestionnaireId(null);
        questionnaire.setUserId(userId);
        questionnaire.setDate(new Timestamp(Instant.now().toEpochMilli()));
        Questionnaire questionnaireStored = questionnaireRepository.save(questionnaire);

        questions.forEach(question -> {
            question.setQuestionnaireId(questionnaireStored.getQuestionnaireId());
            storeQuestion(question);
        });

        return questionnaireStored;
    }

    private Question storeQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Questionnaire getQuestionnaire(Long userId, Long questionnaireId) {
        userService.verifyUserExists(userId);
        Optional<Questionnaire> questionnaire = questionnaireRepository.findById(questionnaireId);
        if (questionnaire.isEmpty()) {
            throw new QuestionnaireNotFoundException("The questionnaire having the 'questionnaireId': '" +
                    questionnaireId + "' does not exist");
        }
        return questionnaire.get();
    }

    private QuestionnaireJson loadQuestionnaireFromFile(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            InputStream in = resource.getInputStream();
            byte[] json = in.readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, QuestionnaireJson.class);
        } catch (IOException exception) {
            throw new InternalServerException("Error parsing Json Questionaire");
        }

    }

    public void verifyQuestionnaireExists(Long questionnaireId) {
        if (!questionnaireRepository.existsById(questionnaireId)) {
            throw new UserNotFoundException("The questionnaire having the 'questionnaireId': '" + questionnaireId + "' does not exist");
        }
    }

    protected void completeQuestionnaire(Long userId, Long questionnaireId) {
        Questionnaire questionnaire = getQuestionnaire(userId, questionnaireId);
        questionnaire.setCompleted(true);
        questionnaireRepository.save(questionnaire);
    }
}
