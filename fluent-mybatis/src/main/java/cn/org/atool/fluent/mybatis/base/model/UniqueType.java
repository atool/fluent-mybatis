package cn.org.atool.fluent.mybatis.base.model;

/**
 * 特殊字段类型（在Entity字段中只能定义一个)
 *
 * @author wudarui
 */
public enum UniqueType {
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
