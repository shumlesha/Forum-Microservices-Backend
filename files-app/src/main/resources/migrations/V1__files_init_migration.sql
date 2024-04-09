CREATE TABLE files
(
    id          UUID                        NOT NULL,
    name        VARCHAR(255)                NOT NULL,
    size        BIGINT                      NOT NULL,
    date_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    content     VARCHAR(255)                NOT NULL,
    mime_type   VARCHAR(255)                NOT NULL,
    owner_email VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_files PRIMARY KEY (id)
);