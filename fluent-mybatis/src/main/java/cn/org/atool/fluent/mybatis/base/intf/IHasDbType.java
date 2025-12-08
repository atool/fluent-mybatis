package cn.org.atool.fluent.mybatis.base.intf;

import cn.org.atool.fluent.mybatis.metadata.DbType;

/**
 * 返回返回数据库类型的对象
 *
 * @author darui.wu
 */
public interface IHasDbType {
    /**
     * 返回对应的数据库类型
     *
     * @return DbType
     */
    DbType db();

    /**
     * 变更对应的数据库类型
     *
     * @param dbType DbType
     */
    void db(DbType dbType);
}