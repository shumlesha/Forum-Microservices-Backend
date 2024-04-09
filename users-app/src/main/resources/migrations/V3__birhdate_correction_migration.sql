ALTER TABLE users
    DROP COLUMN birth_date;

ALTER TABLE users
    ADD birth_date date NOT NULL default '2005-01-21';