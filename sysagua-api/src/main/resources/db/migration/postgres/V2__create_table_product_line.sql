create table if not exists product_line(
        id BIGINT NOT NULL primary key,
        name varchar,
        active BOOLEAN
);