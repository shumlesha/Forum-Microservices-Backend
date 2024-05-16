CREATE TABLE favorite_topics
(
    id          UUID                        NOT NULL,
    topic_id    UUID                        NOT NULL,
    owner_email VARCHAR(255)                NOT NULL,
    create_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_favorite_topics PRIMARY KEY (id)
);

ALTER TABLE favorite_topics
    ADD CONSTRAINT FK_FAVORITE_TOPICS_ON_TOPIC FOREIGN KEY (topic_id) REFERENCES topics (id);