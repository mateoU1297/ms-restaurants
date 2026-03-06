CREATE TABLE dishes
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    price         INTEGER      NOT NULL CHECK (price > 0),
    description   VARCHAR(500) NOT NULL,
    url_image     VARCHAR(500) NOT NULL,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    restaurant_id BIGINT       NOT NULL,
    category_id   BIGINT       NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP,
    CONSTRAINT fk_dishes_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
    CONSTRAINT fk_dishes_category FOREIGN KEY (category_id) REFERENCES categories (id)
);