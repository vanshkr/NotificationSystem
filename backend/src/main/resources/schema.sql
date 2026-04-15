CREATE TABLE IF NOT EXISTS delivery_log (
    id            BIGSERIAL PRIMARY KEY,
    event_id      VARCHAR(100) UNIQUE NOT NULL,
    recipient     VARCHAR(255)        NOT NULL,
    channel       VARCHAR(10)         NOT NULL,
    event_type    VARCHAR(50)         NOT NULL,
    status        VARCHAR(20)         NOT NULL,
    provider      VARCHAR(20),
    error_message TEXT,
    created_at    TIMESTAMP DEFAULT NOW(),
    sent_at       TIMESTAMP
);
