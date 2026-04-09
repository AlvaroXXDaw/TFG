
DROP INDEX IF EXISTS idx_diet_days_client_date;
DROP INDEX IF EXISTS idx_diet_meals_day_order;
DROP INDEX IF EXISTS idx_diet_foods_meal_order;
DROP INDEX IF EXISTS idx_diet_optional_fields_day_order;


DROP TABLE IF EXISTS diet_day_optional_fields;
DROP TABLE IF EXISTS diet_meal_foods;
DROP TABLE IF EXISTS diet_meals;
DROP TABLE IF EXISTS diet_days;
