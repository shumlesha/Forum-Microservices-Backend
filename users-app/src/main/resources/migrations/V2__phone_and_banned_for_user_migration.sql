ALTER TABLE users
    ADD banned BOOLEAN;

ALTER TABLE users
    ADD phone_number VARCHAR(255);

UPDATE users SET banned = false
             WHERE banned IS NULL;

ALTER TABLE users
    ALTER COLUMN banned SET NOT NULL;

UPDATE users SET phone_number = '+77777777777'
    WHERE users.phone_number IS NULL;

ALTER TABLE users
    ALTER COLUMN phone_number SET NOT NULL;