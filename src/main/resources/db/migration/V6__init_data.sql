insert into t_role(c_name) values('ROLE_USER');
insert into t_user(c_login, c_password) values('test', '$2a$12$6yjGgEg48XnpOr9itmzxIu1HtmqEhVFopu0bkjjnorwPp.0N2QvOe');
insert into t_user_role(user_id, role_id) VALUES(1,1);
insert into t_category(c_name) values('food'), ('work'), ('freelance'), ('salary'), ('freelance');