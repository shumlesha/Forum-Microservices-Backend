CREATE TABLE notifications
(
    id             UUID                        NOT NULL,
    topic          VARCHAR(255)                NOT NULL,
    content        VARCHAR(255)                NOT NULL,
    receiver_email VARCHAR(255)                NOT NULL,
    create_time    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_read      BOOLEAN                     NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);