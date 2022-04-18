CREATE TABLE social_credits(
    user_id BIGINT,
    chat_id BIGINT,
    rating INTEGER,
    PRIMARY KEY(user_id, chat_id)
);