create schema if not exists fluent_mybatis collate utf8_bin;
use
    fluent_mybatis;

drop table if exists idcard;
CREATE TABLE `idcard`
(
    `id`         bigint(21) unsigned  primary key COMMENT '主键id',
    `code`       varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL,
    `version`    bigint(21) unsigned NOT NULL        default 0 COMMENT '版本锁',
    `is_deleted` bigint(21)                          DEFAULT 0 COMMENT '是否逻辑删除'
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

DROP TABLE IF EXISTS `no_auto_id`;
create table `no_auto_id`
(
    id           varchar(50)          not null primary key,
    column_1     varchar(20)          null,
    lock_version bigint(21) default 0 not null
) ENGINE = InnoDB
  charset = utf8 COMMENT = '非自增主键表';

drop table if exists no_primary;
create table no_primary
(
    column_1 int          NULL,
    column_2 varchar(100) NULL,
    `alias`  varchar(20) DEFAULT NULL
) ENGINE = InnoDB
  charset = utf8 COMMENT = '无主键表';

drop table if exists home_address;
CREATE TABLE home_address
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    student_id   bigint(21)  NOT NULL COMMENT '用户id',
    province     varchar(50)          DEFAULT NULL COMMENT '省份',
    city         varchar(50)          DEFAULT NULL COMMENT '城市',
    district     varchar(50)          DEFAULT NULL COMMENT '区',
    address      varchar(100)         DEFAULT NULL COMMENT '详细住址',
    env          varchar(10) NULL comment '数据隔离环境',
    tenant       bigint      NOT NULL default 0 comment '租户标识',
    gmt_created  datetime             DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime             DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)           DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '学生家庭住址';

drop table if exists student;
create table student
(
    id              bigint(21) unsigned auto_increment comment '主键id'
        primary key,
    desk_mate_id    bigint(21)            null comment '同桌',
    age             int                   null comment '年龄',
    grade           int                   null comment '年级',
    user_name       varchar(45)           null comment '名字',
    gender          tinyint(2)  default 0 null comment '性别, 0:女; 1:男',
    birthday        datetime              null comment '生日',
    phone           varchar(20)           null comment '电话',
    bonus_points    bigint(21)  default 0 null comment '积分',
    status          varchar(32)           null comment '状态(字典)',
    home_county_id  bigint(21)            null comment '家庭所在区县',
    home_address_id bigint(21)            null comment 'home_address外键',
    address         varchar(200)          null comment '家庭详细住址',
    version         varchar(200)          null comment '版本号',
    email           varchar(50) DEFAULT NULL COMMENT '邮箱',
    env             varchar(10)           NULL comment '数据隔离环境',
    tenant          bigint                NOT NULL default 0 comment '租户标识',
    gmt_created     datetime              null comment '创建时间',
    gmt_modified    datetime              null comment '更新时间',
    is_deleted      tinyint(2)  default 0 null comment '是否逻辑删除'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT '学生信息表';

drop table if exists teacher;
create table teacher
(
    id           bigint auto_increment primary key comment '主键id',
    user_name    varchar(20)          null comment '名字',
    gmt_created  datetime             null comment '创建时间',
    gmt_modified datetime             null comment '更新时间',
    is_deleted   tinyint(2) default 0 null comment '是否逻辑删除',
    env          varchar(10)          NULL comment '数据隔离环境'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT '教师信息表';

drop table if exists student_teacher_relation;
create table student_teacher_relation
(
    id           bigint auto_increment primary key,
    student_id   bigint               not null,
    teacher_id   bigint               not null,
    gmt_created  datetime             null comment '创建时间',
    gmt_modified datetime             null comment '更新时间',
    is_deleted   tinyint(2) default 0 null comment '是否逻辑删除',
    env          varchar(10)          NULL comment '数据隔离环境'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
    COMMENT '学生教师关联表';

drop table if exists `county_division`;
CREATE TABLE `county_division`
(
    `id`           bigint(21) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `province`     varchar(50) DEFAULT NULL COMMENT '省份',
    `city`         varchar(50) DEFAULT NULL COMMENT '城市',
    `county`       varchar(50) DEFAULT NULL COMMENT '区县',
    `gmt_created`  datetime    DEFAULT NULL COMMENT '创建时间',
    `gmt_modified` datetime    DEFAULT NULL COMMENT '更新时间',
    `is_deleted`   tinyint(2)  DEFAULT '0' COMMENT '是否逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='区县';

DROP TABLE IF EXISTS `student_score`;
create table `student_score`
(
    id           bigint auto_increment comment '主键ID' primary key,
    student_id   bigint            NOT NULL comment '学号',
    gender       tinyint DEFAULT 0 NOT NULL comment '性别, 0:女; 1:男',
    school_term  int               NULL comment '学期',
    subject      varchar(30)       NULL comment '学科',
    score        int               NULL comment '成绩',
    env          varchar(10)       NULL comment '数据隔离环境',
    tenant       bigint            NOT NULL default 0 comment '租户标识',
    gmt_created  datetime          NOT NULL comment '记录创建时间',
    gmt_modified datetime          NOT NULL comment '记录最后修改时间',
    is_deleted   tinyint DEFAULT 0 NOT NULL comment '逻辑删除标识'
) engine = InnoDB
  default charset = utf8 COMMENT = '学生成绩';

drop table if exists t_member;
CREATE TABLE t_member
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    user_name    varchar(45) DEFAULT NULL COMMENT '名字',
    is_girl      tinyint(1)  DEFAULT 0 COMMENT '0:男孩; 1:女孩',
    age          int         DEFAULT NULL COMMENT '年龄',
    school       varchar(20) DEFAULT NULL COMMENT '学校',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(1)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员表:女孩或男孩信息';

drop table if exists t_member_love;
CREATE TABLE t_member_love
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    girl_id      bigint(21) NOT NULL COMMENT 'member表外键',
    boy_id       bigint(21) NOT NULL COMMENT 'member表外键',
    status       varchar(45) DEFAULT NULL COMMENT '状态',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员恋爱关系';

drop table if exists t_member_favorite;
CREATE TABLE t_member_favorite
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    member_id    bigint(21) NOT NULL COMMENT 'member表外键',
    favorite     varchar(45) DEFAULT NULL COMMENT '爱好: 电影, 爬山, 徒步...',
    gmt_created  datetime    DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime    DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)  DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '成员爱好';

drop table if exists `blob_value`;
create table blob_value
(
    id         bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    blob_value blob null,
    origin     varchar(20),
    `max`      bigint(21),
    `min`      bigint(21)
) ENGINE = InnoDB
  CHARACTER SET = utf8;

drop table if exists `my_enum_type`;
create table `my_enum_type`
(
    id          bigint auto_increment COMMENT '主键id',
    enum_string varchar(20) null COMMENT '枚举类型, 字符',
    `enum-num`  int         null COMMENT '枚举类型, 序号',
    is_deleted  tinyint(2) DEFAULT 0 COMMENT '是否逻辑删除',
    constraint my_enum_type_pk primary key (id)
) ENGINE = InnoDB
  CHARACTER SET = utf8;