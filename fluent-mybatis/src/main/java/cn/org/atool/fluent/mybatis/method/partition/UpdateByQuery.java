package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.metadata.TableMeta;

/**
 * UpdateByQuery
 *
 * @author darui.wu
 */
public class UpdateByQuery extends AbstractMethod {
    public UpdateByQuery(DbType dbType) {
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