CREATE TABLE IF NOT EXISTS product_purchases (
    id BIGINT,
    purchase_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    purchase_price NUMERIC(10, 2) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT pp_pkey PRIMARY KEY (id),
    CONSTRAINT fk_purchase FOREIGN KEY (purchase_id) REFERENCES purchases(purchase_id) ON DELETE CASCADE
);