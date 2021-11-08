-- 创建sequence表
drop table if exists `sequence`;
CREATE TABLE `sequence`
(
    `name`          varchar(50) COLLATE utf8_bin NOT NULL COMMENT '序列的名字',
    `current_value` int(11)                      NOT NULL COMMENT '序列的当前值',
    `increment`     int(11)                      NOT NULL DEFAULT '1' COMMENT '序列的自增值',
    PRIMARY KEY (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- 创建取当前值的函数
DROP FUNCTION IF EXISTS currval;
CREATE FUNCTION currval(seq_name VARCHAR(50))
    RETURNS INTEGER
    LANGUAGE SQL
    DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT '创建取当前值的函数'
BEGIN
    DECLARE value INTEGER;
    SET value = 0;
    SELECT current_value
    INTO value
    FROM sequence
    WHERE name = seq_name;
    RETURN value;
END;

-- 创建取下一个值函数
DROP FUNCTION IF EXISTS nextval;
CREATE FUNCTION nextval (seq_name VARCHAR(50))
    RETURNS INTEGER
    LANGUAGE SQL
    DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT '创建取下一个值函数'
BEGIN
    UPDATE sequence
    SET current_value = current_value + increment
    WHERE name = seq_name;
    RETURN currval(seq_name);
END;
-- 创建更新当前值函数
DROP FUNCTION IF EXISTS setval;
CREATE FUNCTION setval (seq_name VARCHAR(50), value INTEGER)
    RETURNS INTEGER
    LANGUAGE SQL
    DETERMINISTIC
    CONTAINS SQL
    SQL SECURITY DEFINER
    COMMENT '创建更新当前值函数'
BEGIN
    UPDATE sequence
    SET current_value = value
    WHERE name = seq_name;
    RETURN currval(seq_name);
END;

-- 添加一个sequence名称和初始值，以及自增幅度
INSERT INTO sequence VALUES ('testSeq', 0, 1);

/**
DROP PROCEDURE IF EXISTS `countRecord`;
CREATE PROCEDURE `countRecord`(IN minId INT, OUT total INT)
BEGIN
SELECT COUNT(*) INTO total FROM blob_value WHERE id >= minId;
END;
 */