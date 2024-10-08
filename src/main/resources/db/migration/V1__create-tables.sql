create table feed
(
    id          bigint primary key generated always as identity,
    title       varchar(1000) not null,
    site_link   varchar(1000) not null,
    feed_link   varchar(1000) not null unique,
    icon        bytea,
    description text,
    loaded      boolean       not null
);

create table item
(
    id          bigint primary key generated always as identity,
    title    varchar(1000) not null,
    link     varchar(1000) not null,
    description text,
    pub_date timestamp     not null,
    guid     varchar(500)  not null unique,
    feed_id  bigint        not null references feed (id) on delete cascade
);

create table "user"
(
    id       bigint primary key generated always as identity,
    email    varchar(100) not null,
    email_hash varchar(100) not null,
    username varchar(100) not null,
    password varchar(100) not null
);

create table subscription
(
    user_id bigint not null references "user" (id) on delete cascade,
    feed_id bigint not null references feed (id) on delete cascade,
    title varchar(1000),
    constraint subscription_pkey primary key (user_id, feed_id)
);

create table read_item
(
    user_id bigint not null references "user" (id) on delete cascade,
    item_id bigint not null references item (id) on delete cascade,
    constraint read_item_pkey primary key (user_id, item_id)
);

create table star_item
(
    user_id bigint not null references "user" (id) on delete cascade,
    item_id bigint not null references item (id) on delete cascade,
    constraint star_item_pkey primary key (user_id, item_id)
);