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
