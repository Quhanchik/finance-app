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
