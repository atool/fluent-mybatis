package cn.org.atool.fluent.mybatis.base.intf;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.util.List;

/**
 * 根据字段名称, 获取对应条件值
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "unused"})
public interface IDataByColumn {
    /**
     * 获取entity的对应数据库字段的属性值
     *
     * @param column 数据库字段名称
     * @param <T>    属性值类型
     * @return 属性值: 可能是单值, 也可能是值列表
     */
    <T> T valueByColumn(String column);

    /**
     * 对应的实体类
     *
     * @return IEntity Class
     */
    Class<? extends IEntity> entityClass();

    /**
     * 获取单一键值
     *
     * @param column 字段
     * @return 分表键值
     */
    default <T> T getOneValueBy(String column) {
        if (this instanceof IEntity) {
            return this.valueByColumn(column);
        }
        List list = this.valueByColumn(column);
        if (list == null || list.size() == 0) {
            return null;
        } else if (list.size() == 1) {
            return (T) list.get(0);
        } else {
            throw new RuntimeException("More then one value for column[" + column + "]:" + list);
        }
    }
}