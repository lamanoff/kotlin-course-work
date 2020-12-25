CREATE TABLE IF NOT EXISTS chat_storage (
    tag varchar(15),
    from_name varchar(15),
    message varchar(15),
    received_time varchar(50),
    PRIMARY KEY (tag)
);