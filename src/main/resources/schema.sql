CREATE TABLE IF NOT EXISTS one_time_tokens
(
    token_value VARCHAR(36)  NOT NULL PRIMARY KEY,
    username    VARCHAR(128) NOT NULL,
    expires_at  TIMESTAMP    NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_one_time_tokens_username ON one_time_tokens (username);
CREATE INDEX IF NOT EXISTS idx_one_time_tokens_expires_at ON one_time_tokens (expires_at);

CREATE TABLE IF NOT EXISTS account
(
    username VARCHAR(128) NOT NULL PRIMARY KEY,
    email    VARCHAR(256) NOT NULL UNIQUE
);