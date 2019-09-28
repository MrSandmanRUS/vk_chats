drop database chats;
create database chats;
use chats;

create table user (
                      id bigint not null auto_increment,
                      avatar varchar(512),
                      vk_id varchar(64),
                      info text,
                      primary key (id),
                      unique (vk_id)
);
ALTER TABLE `user` CONVERT TO CHARACTER SET utf8mb4;

create table chats (
                       id bigint not null auto_increment,
                       interest varchar(512),
                       preview varchar(1024),
                       link varchar(1024),
					   chat_vk_id bigint,
                       primary key (id),
                       unique (interest)
);
ALTER TABLE `chats` CONVERT TO CHARACTER SET utf8mb4;

create table users_chats (
                      id bigint not null auto_increment,
                      user_id bigint not null,
                      chat_id bigint not null,
                      primary key (id),
                      unique key(user_id, chat_id)
);
ALTER TABLE users_chats CONVERT TO CHARACTER SET utf8mb4;

alter table users_chats add index users_chats_user_id_inx(user_id);
alter table users_chats add index users_chats_chat_id_inx(chat_id);

insert into user (avatar, vk_id) values ("Avatar1","3");
insert into user (avatar, vk_id) values ("Avatar2","231");
insert into user (avatar, vk_id) values ("Avatar3","3244");
insert into user (avatar, vk_id) values ("4444","342");

insert into chats (interest, preview) values ("Music", "232131");
insert into chats (interest, preview) values ("Comp", "222");
insert into chats (interest, preview) values ("Matan", "44");

insert into users_chats (user_id, chat_id) values (1,1);
insert into users_chats (user_id, chat_id) values (1,2);
insert into users_chats (user_id, chat_id) values (2,1);
insert into users_chats (user_id, chat_id) values (3,2);
insert into users_chats (user_id, chat_id) values (3,3);
insert into users_chats (user_id, chat_id) values (4,3);

create table translates (
    id bigint not null auto_increment primary key,
    created_when date,
    english varchar(200) unique,
    russian varchar(200),
    usage_count bigint
);

create index russian_index on translates(russian);
create index created_when_translates_index on translates(created_when);
