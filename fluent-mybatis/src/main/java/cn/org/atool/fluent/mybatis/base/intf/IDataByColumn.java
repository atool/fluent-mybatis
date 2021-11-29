package cn.org.atool.fluent.mybatis.base.intf;

import cn.org.atool.fluent.mybatis.base.IEntity;

/**
 * 根据字段名称, 获取对应条件值
 *
 * @author darui.wu
 */
public interface IDataByColumn {
    /**
     * 获取entity的对应数据库字段的属性值
     *
     * @param column 数据库字段名称
     * @param <T>    属性值类型
     * @return 属性值
     */
    <T> T valueByColumn(String column);

    /**
     * 对应的实体类
     *
     * @return IEntity Class
     */
    Class<? extends IEntity> entityClass();
}