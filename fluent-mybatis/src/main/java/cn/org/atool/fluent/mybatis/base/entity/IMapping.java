package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldType;

import java.util.Optional;

/**
 * EntityMapping基类
 *
 * @author darui.wu
 */
public interface IMapping {
    /**
     * 根据Entity属性换取数据库字段名称
     *
     * @param field 属性名称
     * @return 字段名称
     */
    String findColumnByField(String field);

    /**
     * 返回主键字段名称
     * 如果没有主键字段, 则返回null
     *
     * @return 主键字段名称
     */
    default String findPrimaryColumn() {
        return this.findField(FieldType.PRIMARY_ID).map(c -> c.column).orElse(null);
    }

    /**
     * 返回特定类型字段
     *
     * @param type 字段类型
     * @return 字段映射
     */
    Optional<FieldMapping> findField(FieldType type);
}