CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    role    VARCHAR(255)
);

CREATE TABLE users
(
    id               UUID                        NOT NULL,
    full_name        VARCHAR(255)                NOT NULL,
    birth_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    email            VARCHAR(255)                NOT NULL,
    password         VARCHAR(255)                NOT NULL,
    confirm_password VARCHAR(255)                NOT NULL,
    confirmed        BOOLEAN                     NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE verification_tokens
(
    id              UUID                        NOT NULL,
    token           VARCHAR(255)                NOT NULL,
    expiration_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    email           VARCHAR(255)                NOT NULL,
    CONSTRAINT pk_verification_tokens PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES users (id);