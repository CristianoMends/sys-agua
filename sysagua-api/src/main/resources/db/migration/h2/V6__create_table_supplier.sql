
CREATE TABLE suppliers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    social_reason VARCHAR(250),
    phone VARCHAR(13) NOT NULL,
    active BOOLEAN NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    number VARCHAR(10),
    neighborhood VARCHAR(50),
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(2)
);
