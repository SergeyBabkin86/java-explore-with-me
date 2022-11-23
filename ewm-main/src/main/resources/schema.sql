CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR UNIQUE                                  NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name  VARCHAR                                         NOT NULL,
    email VARCHAR UNIQUE                                  NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    annotation         VARCHAR(2000)                                   NOT NULL,
    category_id        BIGINT REFERENCES categories (id),
    description        VARCHAR(7000)                                   NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE                     NOT NULL,
    lat                REAL                                            NOT NULL,
    lon                REAL                                            NOT NULL,
    paid               BOOLEAN                                         NOT NULL,
    participant_limit  INTEGER                                         NOT NULL,
    request_moderation BOOLEAN                                         NOT NULL,
    title              VARCHAR(120)                                    NOT NULL,
    confirmed_requests INTEGER DEFAULT 0,
    created_on         TIMESTAMP WITHOUT TIME ZONE                     NOT NULL,
    initiator_id       BIGINT REFERENCES users (id),
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    state              VARCHAR(21),
    views              INTEGER
);

CREATE TABLE IF NOT EXISTS requests
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE                     NOT NULL,
    event_id      BIGINT REFERENCES events (id),
    requester_id  BIGINT REFERENCES users (id),
    status        VARCHAR(21)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    pinned BOOLEAN,
    title  VARCHAR(120) UNIQUE                             NOT NULL
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id       BIGINT REFERENCES events (id),
    compilation_id BIGINT REFERENCES compilations (id),
    CONSTRAINT pk_events_compilations PRIMARY KEY (event_id, compilation_id)
);

