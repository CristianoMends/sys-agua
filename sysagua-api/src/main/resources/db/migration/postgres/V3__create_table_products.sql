CREATE TABLE IF NOT EXISTS products (
    product_id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    cost DOUBLE PRECISION NOT NULL,
    unit VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    category_id BIGINT,
    ncm VARCHAR,
    line_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    active BOOLEAN,
    FOREIGN KEY (category_id) REFERENCES product_category (id),
    FOREIGN KEY (line_id) REFERENCES product_line (id)
);
