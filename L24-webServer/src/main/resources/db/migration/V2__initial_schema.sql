drop table client;

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint
);

create table address
(
    id   bigint not null primary key,
    street varchar(150)
);

create table phones
(
    id   bigint not null primary key,
    number varchar(150),
    client_id bigint
);

create sequence phones_id_seq start with 1 increment by 1;
create sequence address_id_seq start with 1 increment by 1;
