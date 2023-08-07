DROP TABLE bat_test_people IF EXISTS;

CREATE TABLE bat_test_people (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
)