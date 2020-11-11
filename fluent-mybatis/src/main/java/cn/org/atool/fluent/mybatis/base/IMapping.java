package cn.org.atool.fluent.mybatis.base;

/**
 * EntityMapping基类
 *
 * @author darui.wu
 */
public interface IMapping {
    /**
     * 根据Entity属性换取数据库字段名称
     *
     * @param field
     * @return
     */
    String findColumnByField(String field);

    /**
     * 返回主键字段名称
     *
     * @return
     */
    String findPrimaryColumn();
}