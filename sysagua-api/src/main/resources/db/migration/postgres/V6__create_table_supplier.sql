
CREATE TABLE IF NOT EXISTS suppliers (
    id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    social_reason VARCHAR(250),
    phone VARCHAR(13) NOT NULL,
    active BOOLEAN NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    number VARCHAR(10),
    neighborhood VARCHAR(50),
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(2),
    trade_name VARCHAR(50),
    state_registration VARCHAR(50),
    municipal_registration VARCHAR(50),
    CONSTRAINT suppliers_pkey PRIMARY KEY (id)
);
