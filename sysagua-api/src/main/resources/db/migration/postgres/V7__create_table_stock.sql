CREATE TABLE IF NOT EXISTS stock (
    id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ) PRIMARY KEY,
    product_id BIGINT,
    initial_quantity INT,
    total_entries INT,
    total_withdrawals INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Última atualização
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);
