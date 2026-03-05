CREATE SCHEMA IF NOT EXISTS restaurant_management;

GRANT USAGE ON SCHEMA restaurant_management TO restaurant_db;
GRANT CREATE ON SCHEMA restaurant_management TO restaurant_db;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA restaurant_management TO restaurant_db;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA restaurant_management TO restaurant_db;
ALTER DEFAULT PRIVILEGES IN SCHEMA restaurant_management GRANT ALL PRIVILEGES ON TABLES TO restaurant_db;
ALTER DEFAULT PRIVILEGES IN SCHEMA restaurant_management GRANT ALL PRIVILEGES ON SEQUENCES TO restaurant_db;