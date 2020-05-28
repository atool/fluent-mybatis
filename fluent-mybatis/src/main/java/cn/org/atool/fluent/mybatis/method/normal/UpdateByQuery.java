package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.method.metadata.FieldMeta;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_UpdateByQuery;

/**
 * UpdateByQuery
 *
 * @author darui.wu
 */
public class UpdateByQuery extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_UpdateByQuery;
    }

    @Override
    public String getMethodSql(Class entity, TableMeta table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.update, statementId(), Map.class)
            .update(table, super.isSpecTable())
            .set(() -> update(table, builder))
            .where(() -> super.whereEntity(table, builder))
            .suffixComment()
            .end(StatementType.update)
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
            .ifThen("ew != null and ew.updates != null", () -> builder.eachJoining(table.getFields(), (field) -> updateField(builder, field)))
            .ifThen("ew != null and ew.sqlSet != null", "${ew.sqlSet}");
    }

    private void updateField(SqlBuilder builder, FieldMeta field) {
        if (isUpdateDefault(field)) {
            builder.value("@column=@property,", field.getUpdate(), field.getColumn());
        } else {
            builder.ifThen("ew.updates.containsKey('@property')", "@column=#{ew.updates.@property},", field.getProperty(), field.getColumn());
        }
    }
}