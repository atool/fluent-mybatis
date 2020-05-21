package cn.org.atool.fluent.mybatis.handler;

import java.io.Serializable;

/**
 * 自定义枚举接口
 *
 * @author darui.wu
 */
public interface IEnum<T extends Serializable> {

    /**
     * 枚举数据库存储值
     *
     * @return
     */
    T getValue();
}