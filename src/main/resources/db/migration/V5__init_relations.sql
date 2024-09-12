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

