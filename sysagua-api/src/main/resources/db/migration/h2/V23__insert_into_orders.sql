INSERT INTO orders (customer_id, delivery_person_id, delivery_status, received_amount, total_amount, balance, payment_method, created_at, finished_at, canceled_at, description)
VALUES (1, 1, 'PENDING', 50.00, 50.00, 0, 'MONEY', NOW(), NULL, NULL, 'sem descrição');

INSERT INTO orders (customer_id, delivery_person_id, delivery_status, received_amount, total_amount, balance, payment_method, created_at, finished_at, canceled_at, description)
VALUES (2, 2, 'LATE', 50.00, 50.00, 0, 'MONEY', NOW(), NULL, NULL, 'sem descrição');
