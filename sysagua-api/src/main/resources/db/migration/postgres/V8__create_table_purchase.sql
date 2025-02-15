CREATE TABLE IF NOT EXISTS purchases (
    purchase_id BIGINT NOT NULL,
    supplier_id BIGINT,
    total_amount NUMERIC(10,2),
    paid_amount NUMERIC(10,2),
    total_value NUMERIC(10,2),
    active BOOLEAN DEFAULT TRUE,
    entry_at TIMESTAMP,
    created_at TIMESTAMP,
    finished_At TIMESTAMP,
    canceled_at TIMESTAMP,
    description VARCHAR(255),
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    nfe VARCHAR(11),
    CONSTRAINT purchases_pkey PRIMARY KEY (purchase_id),
    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);