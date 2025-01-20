CREATE TABLE IF NOT EXISTS customers
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(13) NOT NULL,
    created_at DATE NOT NULL,
    active BOOLEAN NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    number VARCHAR(10),
    neighborhood VARCHAR(50),
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(2),
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);