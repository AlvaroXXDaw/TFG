CREATE TABLE IF NOT EXISTS schedule_slots (
  id UUID PRIMARY KEY,
  slot_time TIME NOT NULL UNIQUE
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000001'::uuid, TIME '09:00'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '09:00'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000002'::uuid, TIME '10:30'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '10:30'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000003'::uuid, TIME '12:00'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '12:00'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000004'::uuid, TIME '13:30'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '13:30'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000005'::uuid, TIME '15:00'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '15:00'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000006'::uuid, TIME '16:30'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '16:30'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000007'::uuid, TIME '18:00'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '18:00'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000008'::uuid, TIME '19:30'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '19:30'
);

INSERT INTO schedule_slots (id, slot_time)
SELECT '70000000-0000-0000-0000-000000000009'::uuid, TIME '21:00'
WHERE NOT EXISTS (
  SELECT 1 FROM schedule_slots WHERE slot_time = TIME '21:00'
);
