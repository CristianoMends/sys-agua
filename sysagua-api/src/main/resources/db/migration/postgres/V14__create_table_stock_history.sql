CREATE TABLE stock_history (
    id BIGINT NOT NULL,
    Type VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INT NOT NULL,
    stock_id BIGINT,
    responsible_id UUID,
    CONSTRAINT stock_history_pkey PRIMARY KEY (id),
    CONSTRAINT fk_stock_history_user FOREIGN KEY (responsible_id) REFERENCES users(user_id),
    CONSTRAINT fk_stock_history_stock FOREIGN KEY (stock_id) REFERENCES stock(id)
);
