DROP TABLE bat_test_person IF EXISTS;

CREATE TABLE bat_test_person (
--    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    person_id BIGINT PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);