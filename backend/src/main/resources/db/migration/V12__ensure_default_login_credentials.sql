UPDATE clients
SET email = 'alvaro@admin.com'
WHERE id = '33333333-3333-3333-3333-333333333333'::uuid
  AND email <> 'alvaro@admin.com'
  AND NOT EXISTS (
    SELECT 1 FROM clients WHERE email = 'alvaro@admin.com'
  );

UPDATE clients
SET password_hash = '$2a$10$48yJIxScSScEXagEc5.hoOXJV8vPyoIqeEnlesgrE05K/LnY5EHWW',
    role = 'ADMIN'
WHERE email = 'alvaro@admin.com';

INSERT INTO clients (id, name, email, join_date, password_hash, role, phone, birth_date)
SELECT
  CASE
    WHEN EXISTS (
      SELECT 1 FROM clients WHERE id = '33333333-3333-3333-3333-333333333333'::uuid
    ) THEN gen_random_uuid()
    ELSE '33333333-3333-3333-3333-333333333333'::uuid
  END,
  'Admin Club',
  'alvaro@admin.com',
  DATE '2025-12-01',
  '$2a$10$48yJIxScSScEXagEc5.hoOXJV8vPyoIqeEnlesgrE05K/LnY5EHWW',
  'ADMIN',
  NULL,
  DATE '1990-01-01'
WHERE NOT EXISTS (
  SELECT 1 FROM clients WHERE email = 'alvaro@admin.com'
);

UPDATE clients
SET email = 'alvaro@example.com'
WHERE id = '11111111-1111-1111-1111-111111111111'::uuid
  AND email <> 'alvaro@example.com'
  AND NOT EXISTS (
    SELECT 1 FROM clients WHERE email = 'alvaro@example.com'
  );

UPDATE clients
SET password_hash = '$2a$10$48yJIxScSScEXagEc5.hoOXJV8vPyoIqeEnlesgrE05K/LnY5EHWW',
    role = 'MEMBER'
WHERE email = 'alvaro@example.com';

INSERT INTO clients (id, name, email, join_date, password_hash, role, phone, birth_date)
SELECT
  CASE
    WHEN EXISTS (
      SELECT 1 FROM clients WHERE id = '11111111-1111-1111-1111-111111111111'::uuid
    ) THEN gen_random_uuid()
    ELSE '11111111-1111-1111-1111-111111111111'::uuid
  END,
  'Alvaro Martinez',
  'alvaro@example.com',
  DATE '2026-01-15',
  '$2a$10$48yJIxScSScEXagEc5.hoOXJV8vPyoIqeEnlesgrE05K/LnY5EHWW',
  'MEMBER',
  NULL,
  DATE '1995-06-15'
WHERE NOT EXISTS (
  SELECT 1 FROM clients WHERE email = 'alvaro@example.com'
);
