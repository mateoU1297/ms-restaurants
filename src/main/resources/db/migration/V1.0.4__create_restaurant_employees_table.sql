CREATE TABLE restaurant_employees
(
    id            BIGSERIAL PRIMARY KEY,
    employee_id   BIGINT    NOT NULL,
    restaurant_id BIGINT    NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_restaurant_employee UNIQUE (employee_id, restaurant_id)
);