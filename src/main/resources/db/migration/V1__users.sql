CREATE SEQUENCE users_seq
    START WITH 1000
    CACHE 128
    NO CYCLE;

CREATE TABLE users(
    uid bigint PRIMARY KEY DEFAULT nextval('users_seq'),
    tname json NOT NULL DEFAULT '{}'::json,
    tid VARCHAR(32) NOT NULL UNIQUE,
    gh_token VARCHAR(128) NOT NULL DEFAULT ''
);

ALTER SEQUENCE users_seq OWNED BY users.uid;