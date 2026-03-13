CREATE TABLE orders
(
    id            BIGSERIAL PRIMARY KEY,
    client_id     BIGINT      NOT NULL,
    restaurant_id BIGINT      NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP
);

CREATE TABLE order_dishes
(
    id       BIGSERIAL PRIMARY KEY,
    order_id BIGINT  NOT NULL,
    dish_id  BIGINT  NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    CONSTRAINT fk_order_dishes_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_dishes_dish FOREIGN KEY (dish_id) REFERENCES dishes (id)
);