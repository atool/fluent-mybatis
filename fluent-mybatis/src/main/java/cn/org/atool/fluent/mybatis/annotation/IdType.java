package cn.org.atool.fluent.mybatis.annotation;

import lombok.Getter;

/**
 * 生成ID类型枚举类
 *
 * @author darui.wu
 */
@Getter
public enum IdType {
    /**
     * 数据库ID自增
     */
    AUTO,
    /**
     * 该类型为未设置主键类型(将跟随全局)
     */
    NONE,
    /**
     * 全局唯一ID (idWorker)
     */
    ID_WORKER;
}