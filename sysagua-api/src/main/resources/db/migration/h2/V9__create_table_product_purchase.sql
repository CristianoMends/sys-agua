CREATE TABLE product_purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT,
    product_id BIGINT,
    unit_price NUMERIC(10,2) NOT NULL,
    quantity INT NOT NULL,
    total NUMERIC(10,2),
    FOREIGN KEY (purchase_id) REFERENCES purchases(id) ON DELETE CASCADE
);

