create table if not exists product_line(
        id BIGINT NOT NULL AUTO_INCREMENT primary key,
        name varchar,
        active BOOLEAN
);