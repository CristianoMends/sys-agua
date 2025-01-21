

create table if not EXISTS product_category(
    id BIGINT NOT NULL AUTO_INCREMENT primary key,
    name varchar,
    active Boolean
);