package cn.org.atool.fluent.mybatis.functions;

/**
 * 动态处理表名称, 可以用于自定义分表, 或内部子查询等场景
 *
 * @author darui.wu
 */
public interface DynamicTable {
    /**
     * 获取表名称
     *
     * @param origName 传入的原始表名称
     * @return 动态处理过的表名称
     */
    String tableName(String origName);

    /**
     * 原样返回原始表
     */
    DynamicTable NoneDynamic = origName -> origName;
}