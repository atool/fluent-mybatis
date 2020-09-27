create schema if not exists fluent_mybatis collate utf8_bin;
use fluent_mybatis;

create table if not exists address
(
	id bigint(21) unsigned auto_increment
		primary key,
	address varchar(45) null,
	is_deleted tinyint(2) not null,
	gmt_created datetime null,
	gmt_modified datetime null
);

create table if not exists t_user
(
	id bigint(21) unsigned auto_increment,
	user_name varchar(45) not null,
	address_id bigint(21) null,
	gmt_created datetime not null,
	gmt_modified datetime not null,
	is_deleted tinyint(2) not null,
	age int null,
	version varchar(45) null,
	constraint id_UNIQUE
		unique (id)
);

alter table t_user
	add primary key (id);

create table if not exists no_auto_id
(
    id varchar(50) not null
        primary key,
    column_1 varchar(20) null
);


create table if not exists no_primary
(
    column_1 int null,
    column_2 varchar(100) null
);