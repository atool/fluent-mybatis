package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.UniqueFieldType;
import cn.org.atool.fluent.mybatis.metadata.DbType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * EntityMapping基类
 *
 * @author darui.wu
 */
public interface IMapping {
    /**
     * 返回数据库表名
     */
    Supplier<String> table();

    /**
     * 返回表名
     */
    default String getTableName() {
        return this.table().get();
    }

    /**
     * 返回对应的数据库类型
     */
    DbType getDbType();

    /**
     * 返回数据库字段映射关系
     *
     * @return Map<String, FieldMapping>
     */
    Map<String, FieldMapping> getColumnMap();

    /**
     * 返回用 ', ' 连接好的所有字段
     */
    String getSelectAll();

    /**
     * 根据Entity属性换取数据库字段名称
     *
     * @param field 属性名称
     * @return 字段名称
     */
    String columnOfField(String field);

    /**
     * 返回特定类型字段
     *
     * @param type 字段类型
     * @return 字段映射
     */
    Optional<FieldMapping> findField(UniqueFieldType type);

    /**
     * 返回实体类对应的所有数据库字段列表
     *
     * @return 数据库字段列表
     */
    List<String> getAllColumns();

    /**
     * 返回所有字段列表
     *
     * @return 所有字段列表
     */
    List<FieldMapping> allFields();

    /**
     * 实体类
     *
     * @return entity class
     */
    Class<IEntity> entityClass();

    /**
     * 返回主键字段名称
     * 如果没有主键字段, 则返回null
     *
     * @param nullError 为空时抛出异常
     * @return 主键字段名称
     */
    default String primaryId(boolean nullError) {
        String id = this.findField(UniqueFieldType.PRIMARY_ID).map(c -> c.column).orElse(null);
        if (id == null && nullError) {
            throw new RuntimeException("the primary not found.");
        } else {
            return id;
        }
    }

    /**
     * 乐观锁字段
     *
     * @return ignore
     */
    default String versionField() {
        return this.findField(UniqueFieldType.LOCK_VERSION)
            .map(m -> m.column).orElse(null);
    }

    /**
     * 逻辑删除字段
     *
     * @return ignore
     */
    default String logicDeleteField() {
        return this.findField(UniqueFieldType.LOGIC_DELETED)
            .map(c -> c.column).orElse(null);
    }

    /**
     * 逻辑删除字段是否为 Long 型
     *
     * @return ignore
     */
    default boolean longTypeOfLogicDelete() {
        return this.findField(UniqueFieldType.LOGIC_DELETED)
            .map(m -> m.javaType == Long.class)
            .orElse(false);
    }
}