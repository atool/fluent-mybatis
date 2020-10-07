package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;

/**
 * SelectList: 查询满足条件所有数据
 *
 * @author darui.wu
 * @create 2020/5/18 12:07 下午
 */
public class SelectList extends AbstractMethod {
    public SelectList(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return "";
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        return "";
    }
}