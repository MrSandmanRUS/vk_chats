drop database chats;
create database chats;
use chats;

create table user (
                      id bigint not null auto_increment,
                      name varchar(128),
                      vk_id varchar(64),
                      info text
);

create table users_chats (
                      id bigint not null auto_increment,
                      user_id bigint not null,
                      chat_id bigint not null
);

create table chats (
                       id bigint not null auto_increment,
                       interest varchar(512)
);