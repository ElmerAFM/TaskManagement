create table Users
(
    userID    serial PRIMARY KEY,
    firstName varchar(50),
    lastName  varchar(50),
    email     varchar(50),
    password  varchar(50)
);