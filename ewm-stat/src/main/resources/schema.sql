CREATE TABLE IF NOT EXISTS stat
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    app       VARCHAR(255)                                    NOT NULL,
    uri       VARCHAR(512)                                    NOT NULL,
    ip        VARCHAR(512)                                    NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE                     NOT NULL
);