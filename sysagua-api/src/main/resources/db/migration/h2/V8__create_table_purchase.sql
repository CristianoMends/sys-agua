CREATE TABLE purchases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id BIGINT,
    total NUMERIC(10,2),
    paid_amount NUMERIC(10,2),
    balance NUMERIC(10,2),
    active BOOLEAN DEFAULT TRUE,
    entry_at TIMESTAMP,
    created_at TIMESTAMP,
    finished_At TIMESTAMP,
    canceled_at TIMESTAMP,
    description VARCHAR(255),
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    nfe VARCHAR(11),
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);



