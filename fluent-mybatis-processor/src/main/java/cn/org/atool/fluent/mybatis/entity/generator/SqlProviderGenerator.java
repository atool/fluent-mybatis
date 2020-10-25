package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.BaseSqlProvider;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.mapper.FluentConst;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.SqlProviderUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Map_StrObj;
import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Map_StrStr;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.listIndexEl;
import static java.util.stream.Collectors.joining;

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
    protected void staticImport(JavaFile.Builder builder) {
        super.staticImport(builder);
        builder.addStaticImport(MybatisUtil.class, "*");
        builder.addStaticImport(SqlProviderUtils.class, "*");
        builder.addStaticImport(FluentConst.class, "*");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(BaseSqlProvider.class);
        // provider method
        builder.addMethod(this.m_insert());
        builder.addMethod(this.m_insertBatch());
        builder.addMethod(this.m_updateById());

        //Override method
        builder.addMethod(this.m_updateDefaults());
        builder.addMethod(this.m_tableName());
        builder.addMethod(this.m_idColumn());
        builder.addMethod(this.m_allFields());
        builder.addMethod(this.m_dbType());
    }

    private MethodSpec m_updateDefaults() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("updateDefaults")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(parameterizedType(List.class, String.class))
            .addParameter(CN_Map_StrStr, "updates");

        builder.addStatement("$T<String> sets = new $T<>()", List.class, ArrayList.class);
        for (FieldColumn field : this.fluent.getFields()) {
            if (isBlank(field.getUpdate())) {
                continue;
            }
            builder.addCode("if (!updates.containsKey($S)) {\n", field.getProperty());
            builder.addCode("\tsets.add($S);\n", field.getColumn() + " = " + field.getUpdate());
            builder.addCode("}\n");
        }
        return builder.addStatement("return sets").build();
    }

    private MethodSpec m_updateById() {
        MethodSpec.Builder builder = super.sqlMethod(M_updateById, false)
            .addParameter(CN_Map_StrObj, Param_Map);
        if (this.ifNotPrimary(builder)) {
            return builder.build();
        }
        builder.addStatement("$T entity = getParas(map, Param_ET)", fluent.className());
        builder.addStatement("assertNotNull(Param_Entity, entity)");
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.UPDATE(this.tableName())");
        builder.addStatement("$T<String> sets = new $T<>()", List.class, ArrayList.class);

        for (FieldColumn field : this.fluent.getFields()) {
            if (field.isPrimary()) {
                continue;
            }
            builder.addCode("if (entity.$L() != null) {\n", field.getMethodName());
            builder.addCode("\tsets.add(\"$L = #{et.$L}\");\n", field.getColumn(), field.getProperty());
            if (notBlank(field.getUpdate())) {
                builder.addCode("} else {\n");
                builder.addCode("\tsets.add($S);\n", field.getColumn() + " = " + field.getUpdate());
            }
            builder.addCode("}\n");
        }
        builder.addStatement("sql.SET(sets)");
        builder.addStatement("sql.WHERE(\"$L = #{et.$L}\")", fluent.getPrimary().getColumn(), fluent.getPrimary().getProperty());

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_insert() {
        MethodSpec.Builder builder = super.sqlMethod(M_Insert, false)
            .addParameter(fluent.className(), Param_Entity);

        builder
            .addStatement("assertNotNull(Param_Entity, entity)")
            .addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("sql.INSERT_INTO(this.tableName())")
            .addStatement("$T<String> columns = new $T<>()", List.class, ArrayList.class)
            .addStatement("List<String> values = new ArrayList<>()");
        for (FieldColumn field : this.fluent.getFields()) {
            if (notBlank(field.getInsert())) {
                builder.addStatement("columns.add($S)", field.getColumn());
            }
            builder.addCode("if (entity.$L() != null) {\n", field.getMethodName());
            if (isBlank(field.getInsert())) {
                builder.addStatement("\tcolumns.add($S)", field.getColumn());
            }
            builder.addStatement("\tvalues.add($S)", field.getPropertyEl());
            if (notBlank(field.getInsert())) {
                builder.addCode("} else {\n");
                builder.addStatement("\tvalues.add($S)", field.getInsert());
            }
            builder.addCode("}\n");
        }
        builder
            .addStatement("sql.INSERT_COLUMNS(columns)")
            .addStatement("sql.VALUES()")
            .addStatement("sql.INSERT_VALUES(values)");
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_insertBatch() {
        MethodSpec.Builder builder = super.sqlMethod(M_InsertBatch, false)
            .addParameter(ClassName.get(Map.class), Param_Map);

        builder.addStatement("assertNotEmpty(Param_List, map)");
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("$T<$T> entities = getParas(map, Param_List)", List.class, fluent.className())
            .addStatement("sql.INSERT_INTO(this.tableName())")
            .addStatement("sql.INSERT_COLUMNS(this.allFields())")
            .addStatement("sql.VALUES()");

        String values = this.fluent.getFields().stream()
            .map(field -> {
                String variable = listIndexEl("list", field.getProperty(), "index");
                if (notBlank(field.getInsert())) {
                    return String.format("entities.get(index).%s() == null ? %s : %s",
                        field.getMethodName(), '"' + field.getInsert() + '"', variable);
                } else {
                    return variable;
                }
            }).collect(joining(",\n\t"));

        builder.addCode("for (int index = 0; index < entities.size(); index++) {\n");
        builder.addCode("\tif (index > 0) {\n");
        builder.addStatement("\t\tsql.APPEND($S)", ", ");
        builder.addCode("\t}\n");
        builder.addStatement("\tsql.INSERT_VALUES(\n\t$L\n)", values);
        builder.addCode("}\n");

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_tableName() {
        MethodSpec.Builder builder = super.sqlMethod("tableName", true);
        builder.addStatement("return $S", fluent.getTableName());
        return builder.build();
    }

    private MethodSpec m_idColumn() {
        MethodSpec.Builder builder = super.sqlMethod("idColumn", true);
        if (fluent.getPrimary() == null) {
            this.throwPrimaryNoFound(builder);
        } else {
            builder.addStatement("return $S", fluent.getPrimary().getColumn());
        }
        return builder.build();
    }

    private MethodSpec m_allFields() {
        MethodSpec.Builder builder = super.sqlMethod("allFields", true);
        builder.addStatement("return $S", fluent.getAllFields());
        return builder.build();
    }

    private MethodSpec m_dbType() {
        return MethodSpec.methodBuilder("dbType")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(DbType.class)
            .addStatement("return $T.$L", DbType.class, fluent.getDbType().name())
            .build();
    }

    /**
     * 判断实例类是否有主键
     * 没有主键生成 "抛出异常语句"
     *
     * @param builder
     * @return
     */
    private boolean ifNotPrimary(MethodSpec.Builder builder) {
        if (fluent.getPrimary() == null) {
            super.throwPrimaryNoFound(builder);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}