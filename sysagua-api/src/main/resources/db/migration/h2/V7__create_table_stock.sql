CREATE TABLE stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    initial_quantity INT,
    total_entries INT,
    total_withdrawals INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Data de criação
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Última atualização
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);
