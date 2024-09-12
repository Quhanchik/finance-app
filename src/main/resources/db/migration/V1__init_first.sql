CREATE SCHEMA IF NOT EXISTS finance_app;

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
