CREATE TABLE IF NOT EXISTS endpoint_hits
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    app     VARCHAR(255),
    uri     VARCHAR(512),
    ip      VARCHAR(512),
    created TIMESTAMP WITHOUT TIME ZONE
);