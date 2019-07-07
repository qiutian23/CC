create database if not exists CC_practice;
use CC_practice;
drop table if exists poetry_info;
create table if not exists poetry_info(
    title varchar (64) not null comment '标题',
    dynasty varchar (32) not null comment '朝代',
    author varchar (32) not null comment '作者',
    content varchar (5096) not null comment '正文'
)