create table users
(
    id   integer generated always as identity
        constraint users_pk
            primary key,
    name varchar
);

INSERT INTO users VALUES (default, 'Andrey');
INSERT INTO users VALUES (default, 'Oleg');
INSERT INTO users VALUES (default, 'Aleksey');

--HELLO IGOR
