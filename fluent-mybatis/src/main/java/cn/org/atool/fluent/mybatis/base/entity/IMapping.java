package cn.org.atool.fluent.mybatis.base.entity;

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
     *
     * @return 主键字段名称
     */
    String findPrimaryColumn();
}