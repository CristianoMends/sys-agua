CREATE TABLE stock (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product (id),
    price DOUBLE PRECISION,
    cost DOUBLE PRECISION,
    quantity INT,
    entries INT,
    exits INT,
    added_at DATE,
    updated_at DATE
);
