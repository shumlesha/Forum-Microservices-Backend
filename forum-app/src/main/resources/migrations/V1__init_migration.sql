CREATE TABLE categories
(
    id            UUID         NOT NULL,
    name          VARCHAR(255) NOT NULL,
    parent_id     UUID,
    author_email  VARCHAR(255) NOT NULL,
    create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE category_moderators
(
    id           UUID NOT NULL,
    category_id  UUID NOT NULL,
    moderator_id UUID NOT NULL,
    CONSTRAINT pk_category_moderators PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id            UUID         NOT NULL,
    content       VARCHAR(255) NOT NULL,
    topic_id      UUID         NOT NULL,
    author_email  VARCHAR(255) NOT NULL,
    create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE topics
(
    id            UUID         NOT NULL,
    name          VARCHAR(255) NOT NULL,
    category_id   UUID         NOT NULL,
    author_email  VARCHAR(255) NOT NULL,
    create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_topics PRIMARY KEY (id)
);

ALTER TABLE categories
    ADD CONSTRAINT FK_CATEGORIES_ON_PARENT FOREIGN KEY (parent_id) REFERENCES categories (id);

ALTER TABLE category_moderators
    ADD CONSTRAINT FK_CATEGORY_MODERATORS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_TOPIC FOREIGN KEY (topic_id) REFERENCES topics (id);

ALTER TABLE topics
    ADD CONSTRAINT FK_TOPICS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);