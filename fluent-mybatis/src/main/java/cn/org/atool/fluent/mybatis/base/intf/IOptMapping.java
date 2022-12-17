package cn.org.atool.fluent.mybatis.base.intf;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

import java.util.Optional;

/**
 * 返回返回数据库类型的对象
 *
 * @author darui.wu
 */
public interface IOptMapping {
    /**
     * 数据库映射定义
     *
     * @return Optional<IMapping>
     */
    Optional<IMapping> mapping();
}