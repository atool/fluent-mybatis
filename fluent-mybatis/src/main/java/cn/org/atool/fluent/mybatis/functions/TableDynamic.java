package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.intf.IDataByColumn;

/**
 * 动态处理表名称, 可以用于自定义分表, 或内部子查询等场景
 *
 * @author darui.wu
 */
public interface TableDynamic {
    /**
     * 获取表名称
     *
     * @param origName 传入的原始表名称
     * @param data     insert时Entity, 或IQuery/IUpdate相等条件
     * @return 动态处理过的表名称
     */
    String get(String origName, IDataByColumn data);
}