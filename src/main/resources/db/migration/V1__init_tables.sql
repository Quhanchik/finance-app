

create table t_user
(
    id         serial primary key,
    c_login    text not null unique ,
    c_password text not null
);

create table t_role
(
    id     serial primary key,
    c_name text not null unique
);

create table t_category
(
    id     serial primary key,
    c_name text not null
);

create table t_bill
(
    id               serial primary key,
    c_name           text             not null,
    c_description    text             not null,
    creator_id       int references t_user(id),
    c_total_money    double precision not null,
    c_total_income   double precision not null,
    c_total_expenses double precision not null
);

create table t_finance_unit
(
    id            serial primary key,
    c_is_profit   bool                       not null,
    c_money       double precision           not null,
    c_timestamp   timestamp                  not null,
    category_id   int references t_category (id),
    c_description text                       not null,
    bill_id       int references t_bill (id) not null,
    creator_id int references t_user(id) not null
);

create table t_user_role
(
    id      serial primary key,
    user_id int references t_user (id),
    role_id int references t_role (id),
    constraint uk_user_role unique (role_id, user_id)
);

create table t_user_bill
(
    id      serial primary key,
    user_id int references t_user (id) not null,
    bill_id int references t_bill (id) not null,
    constraint uk_user_bill unique (user_id, bill_id)
);


