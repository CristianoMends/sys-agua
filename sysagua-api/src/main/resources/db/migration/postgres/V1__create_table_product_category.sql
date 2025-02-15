create table if not EXISTS product_category(
    id BIGINT NOT NULL primary key,
    name varchar,
    active Boolean
);