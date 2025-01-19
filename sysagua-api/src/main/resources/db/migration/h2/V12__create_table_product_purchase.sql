CREATE TABLE product_purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT,
    product_id BIGINT,
    purchase_price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    total DOUBLE,
    FOREIGN KEY (purchase_id) REFERENCES purchases(purchase_id) ON DELETE CASCADE
);

