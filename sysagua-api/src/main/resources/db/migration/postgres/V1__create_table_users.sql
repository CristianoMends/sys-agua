CREATE TABLE users
(
    user_id uuid NOT NULL,
    name character varying(20) NOT NULL,
    surname character varying(30),
    phone character varying(11),
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    access character varying(255) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_phone_key UNIQUE (phone)
);