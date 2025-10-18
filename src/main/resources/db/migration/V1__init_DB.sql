CREATE TABLE IF NOT EXISTS users (
    id int NOT NULL,
    username varchar(255) NOT NULL,
    passwordHash varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    address varchar(255),
    tgName varchar(255),
    PRIMARY KEY (id)
);