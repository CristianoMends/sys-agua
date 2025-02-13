INSERT INTO orders (customer_id, delivery_person_id, delivery_status, received_amount, total_amount, payment_method, created_at, finished_at, canceled_at, description)
VALUES (1, 1, 'PENDING', 50.00, 50.00, 'MONEY', NOW(), NULL, NULL, 'sem descrição');

INSERT INTO orders (customer_id, delivery_person_id, delivery_status, received_amount, total_amount, payment_method, created_at, finished_at, canceled_at, description)
VALUES (2, 2, 'LATE', 50.00, 50.00, 'MONEY', NOW(), NULL, NULL, 'sem descrição');
