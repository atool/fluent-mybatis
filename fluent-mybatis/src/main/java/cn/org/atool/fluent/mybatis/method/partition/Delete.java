package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.metadata.TableMeta;

/**
 * 物理删除逻辑
 *
 * @author wudarui
 */
public class Delete extends AbstractMethod {
    public Delete(DbType dbType) {
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