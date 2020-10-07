package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.metadata.DbType;

import static cn.org.atool.fluent.mybatis.method.partition.StatementId.Method_DeleteSpec;

/**
 * DeleteInPartition: 分库删除
 *
 * @author darui.wu
 */
public class DeleteSpec extends Delete {
    public DeleteSpec(DbType dbType) {
        super(dbType);
    }

    @Override
    public String statementId() {
        return Method_DeleteSpec;
    }

    protected boolean isSpecTable() {
        return true;
    }
}