create table item
(
    id          bigint primary key generated always as identity,
    title       varchar(1000) not null,
    link        varchar(1000) not null,
    description text
);