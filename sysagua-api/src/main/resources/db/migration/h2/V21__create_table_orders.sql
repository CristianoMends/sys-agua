CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    delivery_person_id BIGINT,
    delivery_status VARCHAR(50),
    payment_status VARCHAR(50),
    received_amount NUMERIC(10,2),
    total_amount NUMERIC(10,2),
    balance NUMERIC(10, 2),
    payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP,
    canceled_at TIMESTAMP,
    description VARCHAR(255),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_orders_delivery_person FOREIGN KEY (delivery_person_id) REFERENCES delivery_persons(id)
);
