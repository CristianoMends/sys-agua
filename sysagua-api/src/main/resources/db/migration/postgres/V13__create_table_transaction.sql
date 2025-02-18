CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_id BIGINT,
    purchase_id BIGINT,
    responsible_id UUID,
    CONSTRAINT fk_transactions_user FOREIGN KEY (responsible_id) REFERENCES users(user_id),
    CONSTRAINT transactions_pkey PRIMARY KEY (id),
    CONSTRAINT fk_order_transactions FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_purchase_transactions FOREIGN KEY (purchase_id) REFERENCES purchases(id)
);
