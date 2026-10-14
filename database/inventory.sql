CREATE DATABASE IF NOT EXISTS inventory;
USE inventory;

CREATE TABLE IF NOT EXISTS products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    brand VARCHAR(100),
    quantity INT NOT NULL DEFAULT 0,
    unit_price DECIMAL(10,2),
    expiry_date DATE,
    minimum_stock INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS stock_transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    transaction_type ENUM('IN','OUT') NOT NULL,
    quantity INT NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id)
        REFERENCES products(product_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    password_salt VARCHAR(32) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'STAFF'
);