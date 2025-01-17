CREATE TABLE products (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    category VARCHAR(255),
    created_at DATE,
    updated_at DATE,
    active boolean,
    PRIMARY KEY (product_id)
);
