CREATE TABLE users (
    user_id UUID NOT NULL,
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(30),
    phone VARCHAR(11),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    access VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE (email),
    UNIQUE (phone)
);
