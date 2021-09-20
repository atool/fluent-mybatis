package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.metadata.DbType;

/**
 * 返回返回数据库类型的对象
 *
 * @author darui.wu
 */
public interface IHasDbType {
    /**
     * 返回对应的数据库类型
     */
    DbType db();
}