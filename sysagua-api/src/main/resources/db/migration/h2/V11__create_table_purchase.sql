CREATE TABLE purchases (
    purchase_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT,
    total_value DOUBLE,
    active BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP,
    created_at TIMESTAMP,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);



