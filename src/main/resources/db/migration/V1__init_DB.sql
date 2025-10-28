CREATE TABLE IF NOT EXISTS users (
    id bigserial NOT NULL,
    username varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    passwordHash varchar(255) NOT NULL,
    PRIMARY KEY (id)
);