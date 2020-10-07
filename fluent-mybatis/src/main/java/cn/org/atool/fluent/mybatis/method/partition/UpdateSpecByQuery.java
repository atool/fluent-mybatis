package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.metadata.DbType;

import static cn.org.atool.fluent.mybatis.method.partition.StatementId.Method_UpdateSpecByQuery;

/**
 * UpdateSpecByQuery : 更新指定分表
 *
 * @author darui.wu
 */
public class UpdateSpecByQuery extends UpdateByQuery {
    public UpdateSpecByQuery(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_UpdateSpecByQuery;
    }

    protected boolean isSpecTable() {
        return true;
    }
}