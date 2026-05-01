ALTER TABLE reservations
  ADD COLUMN IF NOT EXISTS stripe_session_id VARCHAR(255);

UPDATE reservations
SET status = 'CONFIRMED'
WHERE status = 'COMPLETED';

CREATE UNIQUE INDEX IF NOT EXISTS uq_reservation_stripe_session
  ON reservations (stripe_session_id);
