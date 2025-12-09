CREATE TABLE additional_participant
(
    id               BINARY(16)   NOT NULL,
    created_at       datetime     NOT NULL,
    updated_at       datetime     NOT NULL,
    usage_history_id BINARY(16)   NOT NULL,
    sex              VARCHAR(255) NOT NULL,
    age_group        VARCHAR(255) NOT NULL,
    count            INT          NOT NULL,
    CONSTRAINT pk_additionalparticipant PRIMARY KEY (id)
);

ALTER TABLE additional_participant
    ADD CONSTRAINT FK_ADDITIONALPARTICIPANT_ON_USAGE_HISTORY FOREIGN KEY (usage_history_id) REFERENCES usage_history (id);