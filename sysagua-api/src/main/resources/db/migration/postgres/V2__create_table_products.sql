CREATE TABLE IF NOT EXISTS products
(
    product_id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(255) NOT NULL,
    unit character varying(255) NOT NULL,
    brand character varying(255),
    category character varying(255),
    registered_at date,
    updated_at date,
    active BOOLEAN,
    CONSTRAINT products_pkey PRIMARY KEY (product_id)
);