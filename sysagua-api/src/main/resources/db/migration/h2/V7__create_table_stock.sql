CREATE TABLE stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    price DOUBLE,
    cost DOUBLE,
    quantity INT,
    entries INT,
    exits INT,
    added_at DATE,
    updated_at DATE,
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);
