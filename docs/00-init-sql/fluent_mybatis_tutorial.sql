create schema fluent_mybatis_tutorial;
use fluent_mybatis_tutorial;

drop table if exists user;
CREATE TABLE user  (
  id bigint(21) unsigned auto_increment primary key COMMENT '主键id',
  avatar varchar(255) DEFAULT NULL COMMENT '头像',
  account varchar(45) DEFAULT NULL COMMENT '账号',
  password varchar(45) DEFAULT NULL COMMENT '密码',
  user_name varchar(45) DEFAULT NULL COMMENT '名字',
  birthday datetime DEFAULT NULL COMMENT '生日',
  e_mail varchar(45) DEFAULT NULL COMMENT '电子邮件',
  phone varchar(20) DEFAULT NULL COMMENT '电话',
  bonus_points bigint(21) DEFAULT 0 COMMENT '会员积分',
  status varchar(32) DEFAULT NULL COMMENT '状态(字典)',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '更新时间',
  is_deleted tinyint(2) DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户表';

drop table  if exists receiving_address;
CREATE TABLE receiving_address  (
  id bigint(21) unsigned auto_increment primary key COMMENT '主键id',
  user_id bigint(21) NOT NULL COMMENT '用户id',
  province varchar(50) DEFAULT NULL COMMENT '省份',
  city varchar(50) DEFAULT NULL COMMENT '城市',
  district varchar(50) DEFAULT NULL COMMENT '区',
  detail_address varchar(100) DEFAULT NULL COMMENT '详细住址',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '更新时间',
  is_deleted tinyint(2) DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户收货地址';