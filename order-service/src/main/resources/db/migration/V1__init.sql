 CREATE TABLE orders (
     id SERIAL NOT NULL PRIMARY KEY,
     user_id VARCHAR(255),
     email VARCHAR(255),
     name VARCHAR(255),
     status VARCHAR(64)
 );

 CREATE TABLE order_items (
    id SERIAL NOT NULL PRIMARY KEY,
    order_id SERIAL NOT NULL REFERENCES orders(id),
    code VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    external_id VARCHAR(255) NOT NULL,
    expected_delivery_date TIMESTAMP,
    price NUMERIC(19, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL
 );