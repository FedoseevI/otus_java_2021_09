create table users
(
    id   bigserial not null primary key,
    username varchar(50),
    password varchar(100)
);

create table messages
(
    id   bigserial not null primary key,
    message_text varchar(4000),
    message_send_time TIMESTAMP,
    user_from bigserial not null references users(id),
    user_to bigserial not null references users(id)
);