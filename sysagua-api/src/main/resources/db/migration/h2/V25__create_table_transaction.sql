CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    type VARCHAR(20) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP,
    canceled_at TIMESTAMP,
    order_id BIGINT,
    purchase_id BIGINT,
    CONSTRAINT fk_order_transactions FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_purchase_transactions FOREIGN KEY (purchase_id) REFERENCES purchases(purchase_id)
);
