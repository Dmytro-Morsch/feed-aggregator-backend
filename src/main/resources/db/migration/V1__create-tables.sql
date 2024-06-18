create table feed
(
    id          bigint primary key generated always as identity,
    title       varchar(1000) not null,
    site_link   varchar(1000) not null,
    feed_link varchar(1000) not null unique,
    description text
);

create table item
(
    id          bigint primary key generated always as identity,
    title       varchar(1000)               not null,
    link        varchar(1000)               not null,
    description text,
    feed_id     bigint references feed (id) not null
);