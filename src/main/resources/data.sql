DROP TABLE items IF exists cascade;
DROP TABLE customers IF exists cascade;

CREATE TABLE customers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  percentage_discount INT DEFAULT 0,
  type int NOT NULL
);

CREATE TABLE items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  price DECIMAL NOT NULL,
  type int NOT NULL,
  customer_id INT NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);