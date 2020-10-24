create schema if not exists fluent_mybatis collate utf8_bin;

drop table if exists no_auto_id;
create table no_auto_id
(
    id       varchar(50) not null primary key,
    column_1 varchar(20) null
) ENGINE = InnoDB charset = utf8 COMMENT = '非自增主键表';

drop table if exists no_primary;
create table no_primary
(
    column_1 int          null,
    column_2 varchar(100) null
) ENGINE = InnoDB charset = utf8 COMMENT = '无主键表';

drop table if exists receiving_address;
CREATE TABLE receiving_address
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    user_id      bigint(21) NOT NULL COMMENT '用户id',
    province     varchar(50)  DEFAULT NULL COMMENT '省份',
    city         varchar(50)  DEFAULT NULL COMMENT '城市',
    district     varchar(50)  DEFAULT NULL COMMENT '区',
    address      varchar(100) DEFAULT NULL COMMENT '详细住址',
    gmt_created  datetime     DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime     DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)   DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '用户收货地址';

drop table if exists t_user;
CREATE TABLE t_user
(
    id           bigint(21) unsigned auto_increment primary key COMMENT '主键id',
    age          int          DEFAULT NULL COMMENT '年龄',
    grade        int          DEFAULT NULL COMMENT '等级',
    avatar       varchar(255) DEFAULT NULL COMMENT '头像',
    account      varchar(45)  DEFAULT NULL COMMENT '账号',
    password     varchar(45)  DEFAULT NULL COMMENT '密码',
    user_name    varchar(45)  DEFAULT NULL COMMENT '名字',
    birthday     datetime     DEFAULT NULL COMMENT '生日',
    e_mail       varchar(45)  DEFAULT NULL COMMENT '电子邮件',
    phone        varchar(20)  DEFAULT NULL COMMENT '电话',
    bonus_points bigint(21)   DEFAULT 0 COMMENT '会员积分',
    status       varchar(32)  DEFAULT NULL COMMENT '状态(字典)',
    version      varchar(45)  DEFAULT NULL COMMENT '版本号',
    address_id   bigint(21)   DEFAULT NULL COMMENT '收货地址id',
    gmt_created  datetime     DEFAULT NULL COMMENT '创建时间',
    gmt_modified datetime     DEFAULT NULL COMMENT '更新时间',
    is_deleted   tinyint(2)   DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB
  CHARACTER SET = utf8 COMMENT = '用户表';

DROP TABLE IF EXISTS `student_score`;
create table `student_score`
(
    id           bigint auto_increment comment '主键ID' primary key,
    student_id   bigint            not null comment '学号',
    gender_man   tinyint default 0 not null comment '性别, 0:女; 1:男',
    school_term  int               null comment '学期',
    subject      varchar(30)       null comment '学科',
    score        int               null comment '成绩',
    gmt_created  datetime          not null comment '记录创建时间',
    gmt_modified datetime          not null comment '记录最后修改时间',
    is_deleted   tinyint default 0 not null comment '逻辑删除标识'
) engine = InnoDB
  default charset = utf8 COMMENT = '学生成绩';

