CREATE TABLE product_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(10,2) NOT NULL,
    total NUMERIC(10,2),
    CONSTRAINT fk_product_orders_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_product_orders_product FOREIGN KEY (product_id) REFERENCES products(product_id)
);
