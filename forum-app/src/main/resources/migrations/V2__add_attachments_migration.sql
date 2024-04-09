CREATE TABLE attachments
(
    id         UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    size       BIGINT       NOT NULL,
    file_id    UUID         NOT NULL,
    message_id UUID         NOT NULL,
    CONSTRAINT pk_attachments PRIMARY KEY (id)
);

ALTER TABLE attachments
    ADD CONSTRAINT FK_ATTACHMENTS_ON_MESSAGE FOREIGN KEY (message_id) REFERENCES messages (id);