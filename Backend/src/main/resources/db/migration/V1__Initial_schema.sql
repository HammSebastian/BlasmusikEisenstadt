-- Initial schema creation for Stadtkapelle Eisenstadt Backend (PostgreSQL version)
-- Created: 2025-08-04

-- Create locations table
CREATE TABLE locations
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    street        VARCHAR(255),
    street_number VARCHAR(20),
    postal_code   VARCHAR(20),
    city          VARCHAR(100),
    country       VARCHAR(100)
);

-- Create about table
CREATE TABLE about
(
    id              BIGSERIAL PRIMARY KEY,
    about_text      TEXT         NOT NULL,
    about_image_url VARCHAR(500) NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create welcome table
CREATE TABLE welcome
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create events table
CREATE TABLE events
(
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     TEXT         NOT NULL,
    date            DATE         NOT NULL,
    event_image_url VARCHAR(500),
    event_type      VARCHAR(50)  NOT NULL CHECK (event_type IN
                                                 ('GIG', 'CONCERT', 'MORNING_PINT', 'EVENING_PINT', 'SERENADE',
                                                  'OTHERS')),
    location_id     BIGINT REFERENCES locations (id),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create members table
CREATE TABLE members
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    instrument    VARCHAR(100),
    avatar_url    VARCHAR(500),
    date_joined   DATE,
    section       VARCHAR(100),
    notes         TEXT,
    phone_number  VARCHAR(20),
    street        VARCHAR(255),
    street_number VARCHAR(20),
    postal_code   VARCHAR(20),
    city          VARCHAR(100),
    country       VARCHAR(100),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP
);

-- Create gallery table
CREATE TABLE gallery
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create images table
CREATE TABLE images
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_events_date ON events (date);
CREATE INDEX idx_events_event_type ON events (event_type);
CREATE INDEX idx_events_location_id ON events (location_id);
CREATE INDEX idx_members_deleted_at ON members (deleted_at);
CREATE INDEX idx_members_section ON members (section);
CREATE INDEX idx_members_instrument ON members (instrument);

-- Hibernate Envers audit tables
CREATE TABLE about_aud
(
    id                  BIGINT  NOT NULL,
    rev                 INTEGER NOT NULL,
    revtype             SMALLINT,
    about_text          TEXT,
    about_text_mod      BOOLEAN,
    about_image_url     VARCHAR(500),
    about_image_url_mod BOOLEAN,
    created_at          TIMESTAMP,
    created_at_mod      BOOLEAN,
    updated_at          TIMESTAMP,
    updated_at_mod      BOOLEAN,
    PRIMARY KEY (id, rev)
);

CREATE TABLE events_aud
(
    id                  BIGINT  NOT NULL,
    rev                 INTEGER NOT NULL,
    revtype             SMALLINT,
    title               VARCHAR(255),
    title_mod           BOOLEAN,
    description         TEXT,
    description_mod     BOOLEAN,
    date                DATE,
    date_mod            BOOLEAN,
    event_image_url     VARCHAR(500),
    event_image_url_mod BOOLEAN,
    event_type          VARCHAR(50),
    event_type_mod      BOOLEAN,
    location_id         BIGINT,
    location_mod        BOOLEAN,
    created_at          TIMESTAMP,
    created_at_mod      BOOLEAN,
    updated_at          TIMESTAMP,
    updated_at_mod      BOOLEAN,
    PRIMARY KEY (id, rev)
);

CREATE TABLE locations_aud
(
    id                BIGINT  NOT NULL,
    rev               INTEGER NOT NULL,
    revtype           SMALLINT,
    name              VARCHAR(255),
    name_mod          BOOLEAN,
    street            VARCHAR(255),
    street_mod        BOOLEAN,
    street_number     VARCHAR(20),
    street_number_mod BOOLEAN,
    postal_code       VARCHAR(20),
    postal_code_mod   BOOLEAN,
    city              VARCHAR(100),
    city_mod          BOOLEAN,
    country           VARCHAR(100),
    country_mod       BOOLEAN,
    PRIMARY KEY (id, rev)
);

CREATE TABLE members_aud
(
    id                BIGINT  NOT NULL,
    rev               INTEGER NOT NULL,
    revtype           SMALLINT,
    first_name        VARCHAR(255),
    first_name_mod    BOOLEAN,
    last_name         VARCHAR(255),
    last_name_mod     BOOLEAN,
    instrument        VARCHAR(100),
    instrument_mod    BOOLEAN,
    avatar_url        VARCHAR(500),
    avatar_url_mod    BOOLEAN,
    date_joined       DATE,
    date_joined_mod   BOOLEAN,
    section           VARCHAR(100),
    section_mod       BOOLEAN,
    notes             TEXT,
    notes_mod         BOOLEAN,
    phone_number      VARCHAR(20),
    phone_number_mod  BOOLEAN,
    street            VARCHAR(255),
    street_mod        BOOLEAN,
    street_number     VARCHAR(20),
    street_number_mod BOOLEAN,
    postal_code       VARCHAR(20),
    postal_code_mod   BOOLEAN,
    city              VARCHAR(100),
    city_mod          BOOLEAN,
    country           VARCHAR(100),
    country_mod       BOOLEAN,
    created_at        TIMESTAMP,
    created_at_mod    BOOLEAN,
    updated_at        TIMESTAMP,
    updated_at_mod    BOOLEAN,
    deleted_at        TIMESTAMP,
    deleted_at_mod    BOOLEAN,
    PRIMARY KEY (id, rev)
);

-- Envers revision table
CREATE TABLE revinfo
(
    rev      INTEGER PRIMARY KEY,
    revtstmp BIGINT
);
