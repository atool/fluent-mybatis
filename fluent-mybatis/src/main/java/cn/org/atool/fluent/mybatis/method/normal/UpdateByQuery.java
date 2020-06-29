package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableFieldMeta;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import cn.org.atool.fluent.mybatis.method.model.StatementType;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_UpdateByQuery;
import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.*;

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
        return Method_UpdateByQuery;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        builder
            .begin(StatementType.update, statementId(), Map.class)
            .checkWrapper()
            .update(table, super.isSpecTable())
            .set(() -> update(table, builder))
            .where(() -> super.whereByWrapper(builder));
        if (super.getDbType().isCanDirectLimit()) {
            builder.ifThen(Wrapper_Page_Not_Null, () -> builder.append(" LIMIT %s ", Wrapper_Paged_Size));
        }
        String xml = builder.end(StatementType.update)
            .toString();
        return xml;
    }

    /**
     * update set 设置部分
     *
     * @param table
     * @param builder
     * @return
     */
    protected SqlBuilder update(TableMeta table, SqlBuilder builder) {
        return builder
            .ifThen(Wrapper_UpdateStr_Not_Null, () -> builder.eachJoining(table.getFields(), (field) -> updateField(builder, field)))
            .ifThen(Wrapper_UpdateStr_Not_Null, Wrapper_UpdateStr_Var);
    }

    private void updateField(SqlBuilder builder, TableFieldMeta field) {
        if (isUpdateDefault(field)) {
            builder.ifThen(Wrapper_Update_Contain_Key, "@column = @property,", field.getUpdate(), field.getColumn());
        }
    }
}