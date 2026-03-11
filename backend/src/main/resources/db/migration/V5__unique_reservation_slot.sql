CREATE UNIQUE INDEX IF NOT EXISTS uq_reservation_slot
    ON reservations (court_id, reservation_date, reservation_time);
