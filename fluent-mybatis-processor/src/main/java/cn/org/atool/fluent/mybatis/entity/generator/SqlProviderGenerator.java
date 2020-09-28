package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.ibatis.jdbc.SQL;

import javax.lang.model.element.TypeElement;

import java.util.List;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNameConst.*;
import static cn.org.atool.fluent.mybatis.method.SqlMethodName.M_Insert;
import static cn.org.atool.fluent.mybatis.method.SqlMethodName.M_InsertBatch;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;
import static java.util.stream.Collectors.*;

public class SqlProviderGenerator extends AbstractGenerator {

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + Suffix_SqlProvider;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Helper);
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    public SqlProviderGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "动态语句封装";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addMethod(this.m_insert());
        builder.addMethod(this.m_insertBatch());
    }

    private MethodSpec m_insert() {
        MethodSpec.Builder builder = super.sqlMethod(M_Insert)
            .addParameter(fluent.className(), "entity")
            .addStatement("$T sql = new SQL()", SQL.class)
            .addStatement("sql.INSERT_INTO($S)", fluent.getTableName());
        for (FieldColumn field : this.fluent.getFields()) {
            builder.addCode("if (entity.$L() != null) {\n", field.getMethodName());
            builder.addCode("\tsql.VALUES($S, $L);\n", field.getColumn(), wrapper(field));
            if (isNotBlank(field.getInsert())) {
                builder.addCode("} else {\n");
                builder.addCode("\tsql.VALUES($S, $S);\n", field.getColumn(), field.getInsert());
            }
            builder.addCode("}\n");
        }
        return builder.addStatement("return sql.toString()")
            .build();
    }

    private MethodSpec m_insertBatch() {
        MethodSpec.Builder builder = super.sqlMethod(M_InsertBatch)
            .addParameter(parameterizedType(ClassName.get(List.class),
                fluent.className()), "entities")
            .addStatement("$T sql = new SQL()", SQL.class)
            .addStatement("sql.INSERT_INTO($S)", fluent.getTableName());
        String columns = this.fluent.getFields().stream()
            .map(FieldColumn::getColumn)
            .map(this::wrapper)
            .collect(joining(",\n\t"));

        String values = this.fluent.getFields().stream()
            .map(field -> {
                if (isNotBlank(field.getInsert())) {
                    return String.format("entity.%s() == null ? %s : %s",
                        field.getMethodName(), wrapper(field.getInsert()), wrapper(field));
                } else {
                    return wrapper(field);
                }
            }).collect(joining(",\n\t\t"));

        builder.addStatement("sql.INTO_COLUMNS(\n\t$L\n)", columns);
        builder.addStatement("boolean first = true");
        builder.addCode("for ($T entity : entities) {\n", fluent.className());
        builder.addCode("\tif (!first) {\n");
        builder.addCode("\t\tsql.ADD_ROW();\n");
        builder.addCode("\t}\n");
        builder.addCode("\tfirst = false;\n");
        builder.addCode("\tsql.INTO_VALUES(\n\t\t$L\n\t);\n", values);
        builder.addCode("}\n");

        return builder.addStatement("return sql.toString()")
            .build();
    }

    private String wrapper(String string) {
        return '"' + string + '"';
    }

    private String wrapper(FieldColumn field) {
        return new StringBuilder()
            .append('"')
            .append("#{")
            .append(field.getProperty())
            .append("}")
            .append('"').toString();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}