
CREATE TABLE threads(
    uid BIGINT REFERENCES users,
    tid VARCHAR(32) NOT NULL,
    subject JSON NOT NULL DEFAULT '{}'::json,
    last_read TIMESTAMP NOT NULL,
    unread BOOLEAN NOT NULL DEFAULT TRUE
    PRIMARY KEY (uid, tid)
);

COMMENT ON TABLE threads IS 'Local storage to sync user threads from GitHub';
COMMENT ON COLUMN threads.uid IS 'User id - notification receiver';
COMMENT ON COLUMN threads.tid IS 'GitHub thread id';
COMMENT ON COLUMN threads.subject IS 'Thread subject details, such as title, url, etc';
COMMENT ON COLUMN threads.last_read IS 'Thread subject last read timestamp';
