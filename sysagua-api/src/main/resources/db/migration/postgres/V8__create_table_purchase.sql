CREATE TABLE IF NOT EXISTS purchases (
    purchase_id BIGINT NOT NULL,
    supplier_id BIGINT,
    total_value NUMERIC(10, 2),
    active BOOLEAN DEFAULT TRUE,
    updated_at TIMESTAMP,
    created_at TIMESTAMP,
    CONSTRAINT purchases_pkey PRIMARY KEY (purchase_id),
    CONSTRAINT fk_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);