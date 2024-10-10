CREATE TABLE storages (
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE inventories (
    id SERIAL NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    brand VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    created TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    storage_id SERIAL REFERENCES storages(id)
);
