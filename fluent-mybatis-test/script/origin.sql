drop table if exists idcard;
CREATE TABLE `idcard`
(
    `id`   bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    `code` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE = InnoDB
  charset = utf8;

drop table if exists person;
CREATE TABLE `person`
(
    `id`        bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    `name`      varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
    `age`       int(11)                             DEFAULT NULL,
    `idcard_id` bigint(21)                          DEFAULT NULL
) ENGINE = InnoDB
  charset = utf8;

drop table if exists nick_name;
create table nick_name
(
    id        bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    nick_name varchar(100) null,
    person_id bigint(21)   null
) ENGINE = InnoDB
  charset = utf8;