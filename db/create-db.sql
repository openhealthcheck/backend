CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    surname VARCHAR(255),
    socialId VARCHAR(255)
);

CREATE TABLE questionnaires
(
    questionnaire_id bigint auto_increment,
    user_id bigint,
    date DATETIME,
    is_completed INTEGER,
    version VARCHAR(255),
    start bigint,
    constraint questionnaries_pk
        primary key (questionnaire_id)
);

CREATE TABLE questions (
    questionnaire_id BIGINT,
    user_id BIGINT,
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_number INTEGER,
    question TEXT,
    description TEXT,
    value VARCHAR(255),
    type VARCHAR(255),
    category VARCHAR(255)
);

CREATE TABLE question_options (
    question_option_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_question_id BIGINT,
    name VARCHAR(255),
    next INTEGER,
    color VARCHAR(255)
);
