
ALTER TABLE reservations DROP CONSTRAINT IF EXISTS fk_reservation_client;
ALTER TABLE reservations
    ADD CONSTRAINT fk_reservation_client
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE;


ALTER TABLE gym_routine_days DROP CONSTRAINT IF EXISTS fk_routine_day_client;
ALTER TABLE gym_routine_days
    ADD CONSTRAINT fk_routine_day_client
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE;


ALTER TABLE password_reset_tokens DROP CONSTRAINT IF EXISTS fk_reset_token_client;
ALTER TABLE password_reset_tokens
    ADD CONSTRAINT fk_reset_token_client
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE;
