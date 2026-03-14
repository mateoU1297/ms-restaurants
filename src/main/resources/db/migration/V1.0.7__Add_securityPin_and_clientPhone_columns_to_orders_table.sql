ALTER TABLE orders
    ADD COLUMN security_pin VARCHAR(6);
ALTER TABLE orders
    ADD COLUMN client_phone VARCHAR(20);