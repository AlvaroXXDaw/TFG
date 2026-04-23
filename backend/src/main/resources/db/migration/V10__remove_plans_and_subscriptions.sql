-- Eliminamos las columnas relacionadas con suscripciones/planes
ALTER TABLE clients DROP COLUMN IF EXISTS plan;
ALTER TABLE clients DROP COLUMN IF EXISTS subscription_name;
ALTER TABLE clients DROP COLUMN IF EXISTS next_billing_date;
