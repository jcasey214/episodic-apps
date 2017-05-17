create table users (
  id bigint not null auto_increment primary key,
  email varchar(255) not null unique
);