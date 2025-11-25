CREATE DATABASE IF NOT EXISTS proyecto_carts;
USE proyecto_carts;

CREATE TABLE carts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    total DECIMAL(10,2) NOT NULL,
    discounted_total DECIMAL(10,2) NOT NULL,
    user_id INT NOT NULL,
    total_products INT NOT NULL,         -- cantidad de productos distintos en el carrito
    total_quantity INT NOT NULL,     -- suma total de cantidades de productos
);
