package cn.org.atool.fluent.mybatis.base.intf;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

/**
 * 返回返回数据库类型的对象
 *
 * @author darui.wu
 */
public interface IHasMapping {
    /**
     * 数据库映射定义
     *
     * @return IMapping
     */
    IMapping mapping();
}