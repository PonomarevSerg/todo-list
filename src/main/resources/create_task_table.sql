CREATE TABLE IF NOT EXISTS tasks
(
    name             varchar,
    create_date_time date,
    deadline         date,
    user_creator     integer
        constraint tasks_users_fk
            references users,
    state            varchar,
    user_assign_to   integer,
    priority         integer,
    id               integer generated always as identity
        constraint tasks_pk
            primary key
);

INSERT INTO tasks VALUES ('Задача1', null,null, 1,'В работе',3,1);
INSERT INTO tasks VALUES ('Задача2', null,null, 2,'В работе',2,3);
INSERT INTO tasks VALUES ('Задача3', null,null, 2,'В работе',1,2);

