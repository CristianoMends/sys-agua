INSERT INTO purchases (supplier_id, total_amount, paid_amount, active, entry_at, created_at, finished_at, canceled_at, description, payment_method, payment_status, nfe)
VALUES
(1, 5000.00, 5000.00, TRUE, '2024-01-05 08:30:00', '2024-01-04 14:00:00', '2024-01-06 16:00:00', NULL, 'Compra de 1000 galões de água de 20L', 'PIX', 'PAID', '98765432101'),
(2, 2000.00, 2000.00, TRUE, '2024-01-15 10:00:00', '2024-01-14 17:00:00', NULL, NULL, 'Compra de garrafas PET 500ml e 1L', 'DEBIT', 'PAID', '87654321012'),
(3, 1200.00, 1200.00, TRUE, '2024-02-01 09:45:00', '2024-01-31 18:30:00', '2024-02-02 15:30:00', NULL, 'Compra de lacres e tampas para galões', 'MONEY', 'PAID', '76543210987'),
(2, 7500.00, 5000.00, TRUE, '2024-02-10 11:00:00', '2024-02-09 13:00:00', NULL, NULL, 'Compra de filtros e equipamentos de purificação', 'CREDIT', 'PENDING', '65432109876'),
(1, 2500.00, 2500.00, TRUE, '2024-02-20 15:00:00', '2024-02-19 09:00:00', '2024-02-21 14:00:00', NULL, 'Compra de embalagens para transporte', 'PIX', 'PAID', '54321098765');
