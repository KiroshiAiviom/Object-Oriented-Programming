CREATE TABLE clothing_items (
    item_id INT PRIMARY KEY,
    type VARCHAR(10) NOT NULL, -- 'SHIRT' or 'JACKET'
    name VARCHAR(100) NOT NULL,
    size VARCHAR(20) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    sleeve_type VARCHAR(20),
    season VARCHAR(20)
);
