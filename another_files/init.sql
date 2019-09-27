drop database chats;
create database chats;
use chats;

create table user (
                      id bigint not null auto_increment,
                      name varchar(128),
                      avatar varchar(512),
                      vk_id varchar(64),
                      info text,
                      primary key (id),
                      unique (vk_id)
);
ALTER TABLE `user` CONVERT TO CHARACTER SET utf8mb4;


create table users_chats (
                      id bigint not null auto_increment,
                      user_id bigint not null,
                      chat_id bigint not null,
                      constraint primary key (id),
                      unique key(user_id, chat_id)
);
ALTER TABLE `users_chats` CONVERT TO CHARACTER SET utf8mb4;


create table chats (
                       id bigint not null auto_increment,
                       interest varchar(512),
                       preview varchar(1024),
                       link varchar(1024),
                       primary key (id),
                       unique (interest)
);
ALTER TABLE `chats` CONVERT TO CHARACTER SET utf8mb4;

insert into user (name, avatar, vk_id) values ("Kirill", "Avatar1","3");
insert into user (name, avatar, vk_id) values ("Vitaly", "Avatar2","231");
insert into user (name, avatar, vk_id) values ("Anton", "Avatar3","3244");
insert into user (name, avatar, vk_id) values ("Iliya", "4444","342");

insert into users_chats (user_id, chat_id) values (1,1);
insert into users_chats (user_id, chat_id) values (1,2);
insert into users_chats (user_id, chat_id) values (2,1);
insert into users_chats (user_id, chat_id) values (3,2);
insert into users_chats (user_id, chat_id) values (3,3);
insert into users_chats (user_id, chat_id) values (4,3);

insert into chats (interest, preview) values ("Music", "232131");
insert into chats (interest, preview) values ("Comp", "222");
insert into chats (interest, preview) values ("Matan", "44");