create table feed
(
    id          bigint primary key generated always as identity,
    title       varchar(1000) not null,
    site_link   varchar(1000) not null,
    feed_link varchar(1000) not null unique,
    icon      bytea,
    description text
);

create table item
(
    id          bigint primary key generated always as identity,
    title    varchar(1000) not null,
    link     varchar(1000) not null,
    description text,
    pub_date timestamp     not null,
    feed_id  bigint        not null references feed (id) on delete cascade
);