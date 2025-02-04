INSERT INTO purchases (supplier_id, total_value, active, updated_at, created_at, finished_at, canceled_at, description)
VALUES
    (1, 250.00, TRUE, null, CURRENT_TIMESTAMP,null,null,'sem descrição'),
    (2, 180.50, TRUE, null, CURRENT_TIMESTAMP,null,null,'sem descrição'),
    (3, 300.75, FALSE, null, CURRENT_TIMESTAMP,null,null,'sem descrição'),
    (1, 120.00, TRUE, null, '2024-02-19T16:14:31.393545-03:00',null,null,'sem descrição'),
    (4, 500.25, TRUE, CURRENT_TIMESTAMP, '2024-01-19T16:14:31.393545-03:00',null,null,'sem descrição');