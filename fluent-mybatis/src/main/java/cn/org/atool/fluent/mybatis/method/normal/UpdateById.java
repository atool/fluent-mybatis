package cn.org.atool.fluent.mybatis.method.normal;

import cn.org.atool.fluent.mybatis.metadata.FieldInfo;
import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.method.model.StatementType;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.method.model.StatementId.Method_UpdateById;

/**
 * 根据 ID 更新有值字段
 *
 * @author darui.wu
 * @create 2020/1/2 5:30 下午
 */
public class UpdateById extends AbstractMethod {

    @Override
    public String statementId() {
        return Method_UpdateById;
    }

    @Override
    public String getMethodSql(Class entity, TableInfo table) {
        SqlBuilder builder = SqlBuilder.instance();
        String xml = builder
            .begin(StatementType.update, statementId(), Map.class)
            .update(table, super.isSpecTable())
            .set(() -> builder.eachJoining(table.getFields(), (field) -> updateField(builder, field)))
            .where(() -> super.whereById(table, builder))
            .end(StatementType.update)
            .toString();
        return xml;
    }

    private void updateField(SqlBuilder builder, FieldInfo field) {
        if (isUpdateDefault(field)) {
            builder.value("@column=@property,", field.getUpdate(), field.getColumn());
        } else {
            builder.ifThen("et.@property != null", "@column=#{et.@property},", field.getProperty(), field.getColumn());
        }
    }
}