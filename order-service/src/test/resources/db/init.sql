INSERT INTO orders (id, user_id, email, name, status) VALUES
    (1, 'test-user-id','user@user.com', 'User', 'CREATED'),
    (2, 'not-valid-user-id','user@user.com', 'User', 'CREATED');

INSERT INTO order_items (id, order_id, price, quantity, code, brand, external_id, expected_delivery_date, status)
VALUES
    (1, 1, 1.11, 1, 'CODE1', 'BRAND1', '1', '2024-11-03T10:15:30', 'CREATED'),
    (2, 1, 2.22, 2, 'CODE2', 'BRAND2', '2', '2024-12-03T10:15:30', 'CREATED'),
    (3, 2, 1.11, 1, 'CODE1', 'BRAND1', '1', '2024-11-03T10:15:30', 'CREATED'),
    (4, 2, 2.22, 2, 'CODE2', 'BRAND2', '2', '2024-12-03T10:15:30', 'CREATED');
