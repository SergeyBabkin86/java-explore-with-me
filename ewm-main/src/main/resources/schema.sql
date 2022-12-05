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

CREATE TABLE IF NOT EXISTS locations
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    address VARCHAR(2000)                                   NOT NULL,
    lat     REAL                                            NOT NULL,
    lon     REAL                                            NOT NULL,
    rad     INTEGER                                         NOT NULL
);

CREATE TABLE IF NOT EXISTS location_tag
(
    location_id BIGINT REFERENCES locations (id),
    tag_name    VARCHAR(50),
    CONSTRAINT pk_loc_tag PRIMARY KEY (location_id, tag_name)
);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
    declare
        dist      float = 0;
        rad_lat1  float;
        rad_lat2  float;
        theta     float;
        rad_theta float;
    BEGIN
        IF lat1 = lat2 AND lon1 = lon2
        THEN
            RETURN dist;
        ELSE
            -- переводим градусы широты в радианы
            rad_lat1 = pi() * lat1 / 180;
            -- переводим градусы долготы в радианы
            rad_lat2 = pi() * lat2 / 180;
            -- находим разность долгот
            theta = lon1 - lon2;
            -- переводим градусы в радианы
            rad_theta = pi() * theta / 180;
            -- находим длину ортодромии
            dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

            IF dist > 1
            THEN
                dist = 1;
            END IF;

            dist = acos(dist);
            -- переводим радианы в градусы
            dist = dist * 180 / pi();
            -- переводим градусы в километры
            dist = dist * 60 * 1.8524;

            RETURN dist;
        END IF;
    END;
'
    LANGUAGE PLPGSQL;
