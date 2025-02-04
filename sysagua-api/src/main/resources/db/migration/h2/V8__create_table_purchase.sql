CREATE TABLE purchases (
    purchase_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT,
    total_value NUMERIC(10,2),
    active BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP,
    created_at TIMESTAMP,
    finished_At TIMESTAMP,
    canceled_at TIMESTAMP,
    description VARCHAR(255),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);



