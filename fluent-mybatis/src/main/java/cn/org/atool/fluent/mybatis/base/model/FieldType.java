package cn.org.atool.fluent.mybatis.base.model;

/**
 * 特殊字段类型
 *
 * @author wudarui
 */
public enum FieldType {
    /**
     * 主键字段
     */
    PRIMARY_ID,
    /**
     * 逻辑删除字段
     */
    LOGIC_DELETED,
    /**
     * 乐观锁字段
     */
    LOCK_VERSION
}
