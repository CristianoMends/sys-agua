INSERT INTO users (user_id, name, surname, phone, email, password, status, access)
VALUES
(RANDOM_UUID(), 'Bob', 'Johnson', '98888888888', 'bob.johnson@example.com', 'password123', 'ACTIVE', 'MANAGER'),
(RANDOM_UUID(), 'Charlie', 'Brown', '97777777777', 'charlie.brown@example.com', 'password123', 'INACTIVE', 'EMPLOYEE'),
(RANDOM_UUID(), 'David', 'Williams', '96666666666', 'david.williams@example.com', 'password123', 'ACTIVE', 'EMPLOYEE'),
(RANDOM_UUID(), 'Eve', 'Davis', '95555555555', 'eve.davis@example.com', 'password123', 'ACTIVE', 'MANAGER'),
(RANDOM_UUID(), 'Frank', 'Miller', '94444444444', 'frank.miller@example.com', 'password123', 'INACTIVE', 'OWNER'),
(RANDOM_UUID(), 'Grace', 'Wilson', '93333333333', 'grace.wilson@example.com', 'password123', 'ACTIVE', 'EMPLOYEE'),
(RANDOM_UUID(), 'Hannah', 'Moore', '92222222222', 'hannah.moore@example.com', 'password123', 'ACTIVE', 'MANAGER'),
(RANDOM_UUID(), 'Jack', 'Anderson', '90000000000', 'jack.anderson@example.com', 'password123', 'ACTIVE', 'OWNER');