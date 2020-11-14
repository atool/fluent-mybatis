package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.BaseSqlProvider;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.model.InsertList;
import cn.org.atool.fluent.mybatis.base.model.UpdateDefault;
import cn.org.atool.fluent.mybatis.base.model.UpdateSet;
import cn.org.atool.fluent.mybatis.mapper.FluentConst;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.SqlProviderUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.listIndexEl;
import static cn.org.atool.generator.util.ClassNames.*;
import static java.util.stream.Collectors.joining;

/**
 * SqlProviderGenerator: *SqlProvider文件生成
 *
 * @author wudarui
 */
public class SqlProviderFiler extends AbstractFiler {

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_SqlProvider;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Helper);
    }

    public SqlProviderFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "动态语句封装";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        super.staticImport(builder);
        builder.addStaticImport(MybatisUtil.class, "*");
        builder.addStaticImport(SqlProviderUtils.class, "*");
        builder.addStaticImport(FluentConst.class, "*");
        builder.addStaticImport(fluent.mapping(), "*");
        builder.addStaticImport(IRefs.class, "instance");
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
        MethodSpec.Builder builder = super.publicMethod("updateDefaults", true, CN_List_Str)
            .addParameter(CN_Map_StrStr, "updates")
            .addCode("return new $T(updates)\n", UpdateDefault.class);

        for (CommonField field : this.fluent.getFields()) {
            if (notBlank(field.getUpdate())) {
                builder.addCode("\t.add($L, $S)\n", field.getName(), field.getUpdate());
            }
        }
        return builder.addCode("\t.getUpdateDefaults();").build();
    }

    private MethodSpec m_updateById() {
        MethodSpec.Builder builder = super.publicMethod(M_updateById, false, String.class)
            .addParameter(CN_Map_StrObj, Param_Map);
        if (this.ifNotPrimary(builder)) {
            return builder.build();
        }
        builder.addStatement("$T entity = getParas(map, Param_ET)", fluent.entity());
        builder.addStatement("assertNotNull(Param_Entity, entity)");
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.UPDATE(this.tableName())");
        builder.addCode("$T updates = new UpdateSet()", UpdateSet.class);
        for (CommonField field : this.fluent.getFields()) {
            if (!field.isPrimary()) {
                builder.addCode("\n\t.add($L, entity.$L(), $S)",
                    field.getName(), field.getMethodName(), field.getUpdate());
            }
        }
        builder.addCode(";\n");
        builder.addStatement("sql.SET(updates.getUpdates())");
        builder.addStatement("sql.WHERE($L.el(Param_ET))", fluent.getPrimary().getName());

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_insert() {
        MethodSpec.Builder spec = super.publicMethod(M_Insert, false, String.class)
            .addParameter(fluent.entity(), Param_Entity);

        spec.addStatement("assertNotNull(Param_Entity, entity)")
            .addStatement("instance().setEntityByDefault(entity.getClass(), entity)")
            .addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("sql.INSERT_INTO(this.tableName())")
            .addCode("$T inserts = new InsertList()", InsertList.class);
        for (CommonField field : this.fluent.getFields()) {
            spec.addCode("\n\t.add($L, entity.$L(), $S)",
                field.getName(), field.getMethodName(), field.getInsert());
        }
        spec.addCode(";\n");
        spec.addStatement("sql.INSERT_COLUMNS(inserts.columns)")
            .addStatement("sql.VALUES()")
            .addStatement("sql.INSERT_VALUES(inserts.values)");

        return spec.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_insertBatch() {
        MethodSpec.Builder builder = super.publicMethod(M_InsertBatch, false, String.class)
            .addParameter(ClassName.get(Map.class), Param_Map);

        builder.addStatement("assertNotEmpty(Param_List, map)");
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("$T<$T> entities = getParas(map, Param_List)", List.class, fluent.entity())
            .addStatement("super.validateInsertBatch(entities)")
            .addStatement("sql.INSERT_INTO(this.tableName())")
            .addStatement("sql.INSERT_COLUMNS(this.allFields())")
            .addStatement("sql.VALUES()");

        String values = this.fluent.getFields().stream()
            .map(field -> {
                String variable = listIndexEl("list", field.getName(), "index");
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
        return super.publicMethod("tableName", true, String.class)
            .addStatement("return Table_Name")
            .build();
    }

    private MethodSpec m_idColumn() {
        MethodSpec.Builder builder = super.publicMethod("idColumn", true, String.class);
        if (fluent.getPrimary() == null) {
            this.throwPrimaryNoFound(builder);
        } else {
            builder.addStatement("return $L.column", fluent.getPrimary().getName());
        }
        return builder.build();
    }

    private MethodSpec m_allFields() {
        return super.publicMethod("allFields", true, String.class)
            .addStatement("return ALL_JOIN_COLUMNS")
            .build();
    }

    private MethodSpec m_dbType() {
        return super.publicMethod("dbType", true, DbType.class)
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