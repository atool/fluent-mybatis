package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.mapper.MapperUtils;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.TypeElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNameConst.*;
import static cn.org.atool.fluent.mybatis.mapper.MapperUtils.*;
import static cn.org.atool.fluent.mybatis.method.SqlMethodName.*;
import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.COLUMN_MAP;
import static cn.org.atool.fluent.mybatis.method.model.XmlConstant.WRAPPER;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;
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
        builder.addStaticImport(MapperUtils.class, "*");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addMethod(this.m_insert());
        builder.addMethod(this.m_insertBatch());
        builder.addMethod(this.m_deleteByMap());
        builder.addMethod(this.m_deleteById());
        builder.addMethod(this.m_deleteByIds());
        builder.addMethod(this.m_delete());
        /**
         * update
         */
        builder.addMethod(this.m_updateById());
        builder.addMethod(this.m_updateBy());
        /**
         *  select
         */
        builder.addMethod(this.m_findOne());
        builder.addMethod(this.m_findById());
        builder.addMethod(this.m_listByIds());
        builder.addMethod(this.m_listByMap());

        builder.addMethod(this.m_listEntity());
        builder.addMethod(this.m_listMaps());
        builder.addMethod(this.m_listObjs());
        builder.addMethod(this.m_countNoLimit());
        builder.addMethod(this.m_count());
    }

    private MethodSpec m_countNoLimit() {
        MethodSpec.Builder builder = super.sqlMethod(M_countNoLimit).addParameter(ClassName.get(Map.class), Param_Map);

        builder.addStatement("$T data = getWrapperData(map, $S)", WrapperData.class, WRAPPER);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.COUNT($S, data)", fluent.getTableName());
        builder.addStatement("sql.WHERE_GROUP_BY(data)");
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_count() {
        MethodSpec.Builder builder = super.sqlMethod(M_count).addParameter(ClassName.get(Map.class), Param_Map);

        builder.addStatement("$T data = getWrapperData(map, $S)", WrapperData.class, WRAPPER);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.COUNT($S, data)", fluent.getTableName());
        builder.addStatement("sql.WHERE_GROUP_ORDER_BY(data)");
        builder.addStatement("sql.LIMIT(data, false)");
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_listObjs() {
        MethodSpec.Builder builder = super.sqlMethod(M_listObjs).addParameter(ClassName.get(Map.class), Param_Map);

        this.selectByWrapper(builder);
        builder.addStatement("sql.LIMIT(data, false)");
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_listMaps() {
        MethodSpec.Builder builder = super.sqlMethod(M_listMaps).addParameter(ClassName.get(Map.class), Param_Map);

        this.selectByWrapper(builder);
        builder.addStatement("sql.LIMIT(data, false)");
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_listEntity() {
        MethodSpec.Builder builder = super.sqlMethod(M_listEntity).addParameter(ClassName.get(Map.class), Param_Map);

        this.selectByWrapper(builder);
        builder.addStatement("sql.LIMIT(data, false)");
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_listByMap() {
        MethodSpec.Builder builder = super.sqlMethod(M_listByMap).addParameter(ClassName.get(Map.class), Param_Map);

        builder.addStatement("$T<String, Object> where = getParas(map, $S);", Map.class, Param_CM);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.SELECT($S, $S)", fluent.getTableName(), fluent.getAllFields());

        builder.addStatement("sql.WHERE($S, where)", Param_CM);
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_listByIds() {
        MethodSpec.Builder builder = super.sqlMethod(M_listByIds).addParameter(ClassName.get(Map.class), Param_Map);
        if (ifNotPrimary(builder, "no primary found.")) {
            return builder.build();
        }
        builder.addStatement("$T ids = getParas(map, $S);", Collection.class, Param_Coll);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.SELECT($S, $S)", fluent.getTableName(), fluent.getAllFields());
        builder.addStatement("sql.WHERE_PK_IN($S, ids.size())", fluent.getPrimary().getColumn());

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_findOne() {
        MethodSpec.Builder builder = super.sqlMethod(M_findOne).addParameter(ClassName.get(Map.class), Param_Map);
        this.selectByWrapper(builder);
        builder.addStatement("sql.LIMIT(data, false);");

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_findById() {
        MethodSpec.Builder builder = super.sqlMethod(M_findById).addParameter(Serializable.class, Param_Id);
        if (ifNotPrimary(builder, "no primary define found.")) {
            return builder.build();
        }
        builder.addStatement("assertNotNull($S, $L)", Param_Id, Param_Id);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.SELECT($S, $S)", fluent.getTableName(), fluent.getAllFields());
        builder.addStatement("sql.WHERE($S)", fluent.getPrimary().mybatisEl());
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_updateBy() {
        MethodSpec.Builder builder = super.sqlMethod(M_updateBy).addParameter(Map_StrObj, Param_Map);
        builder.addStatement("$T data = getWrapperData(map, $S)", WrapperData.class, WRAPPER);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);

        builder.addStatement("$T<String, String> updates = data.getUpdates()", Map.class);
        builder.addStatement("assertNotEmpty($S, updates)", "updates");

        builder.addStatement("sql.UPDATE($S)", fluent.getTableName());
        builder.addStatement("$T<String> sets = new $T<>()", List.class, ArrayList.class);
        for (FieldColumn field : this.fluent.getFields()) {
            if (isBlank(field.getUpdate())) {
                continue;
            }
            builder.addCode("if (!updates.containsKey($S)) {\n", field.getProperty());
            builder.addCode("\tsets.add($S);\n", field.getColumn() + " = " + field.getUpdate());
            builder.addCode("}\n");
        }
        builder.addStatement("sets.add(data.getUpdateStr())");
        builder.addStatement("sql.SET(sets)");

        builder.addStatement("sql.WHERE_GROUP_ORDER_BY(data)");
        builder.addStatement("sql.LIMIT(data, true)");
        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_updateById() {
        MethodSpec.Builder builder = super.sqlMethod(M_updateById).addParameter(Map_StrObj, Param_Map);
        if (ifNotPrimary(builder, "no primary define found.")) {
            return builder.build();
        }
        builder.addStatement("$T entity = getParas(map, $S)", fluent.className(), Param_ET);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.UPDATE($S)", fluent.getTableName());
        builder.addStatement("$T<String> sets = new $T<>()", List.class, ArrayList.class);

        for (FieldColumn field : this.fluent.getFields()) {
            if (field.isPrimary()) {
                continue;
            }
            builder.addCode("if (entity.$L() != null) {\n", field.getMethodName());
            builder.addCode("\tsets.add(\"$L = #{et.$L}\");\n", field.getColumn(), field.getProperty());
            if (isNotBlank(field.getUpdate())) {
                builder.addCode("} else {\n");
                builder.addCode("\tsets.add($S);\n", field.getColumn() + " = " + field.getUpdate());
            }
            builder.addCode("}\n");
        }
        builder.addStatement("sql.SET(sets)");
        builder.addStatement("sql.WHERE(\"$L = #{et.$L}\")", fluent.getPrimary().getColumn(), fluent.getPrimary().getProperty());

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_deleteByIds() {
        MethodSpec.Builder builder = super.sqlMethod(M_deleteByIds).addParameter(ClassName.get(Map.class), Param_Map);
        if (ifNotPrimary(builder, "no primary define found.")) {
            return builder.build();
        }
        builder.addStatement("$T ids = getParas(map, $S)", Collection.class, Param_Coll);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);

        builder.addStatement("sql.DELETE_FROM($S)", fluent.getTableName());
        builder.addStatement("sql.WHERE_PK_IN($S, ids.size())", fluent.getPrimary().getColumn());

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_delete() {
        MethodSpec.Builder builder = super.sqlMethod(M_Delete).addParameter(ClassName.get(Map.class), Param_Map);
        builder.addStatement("$T data = getWrapperData(map, $S)", WrapperData.class, WRAPPER);

        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.DELETE_FROM($S)", fluent.getTableName());

        builder.addStatement("sql.WHERE_GROUP_ORDER_BY(data)");

        return builder.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_deleteById() {
        MethodSpec.Builder builder = super.sqlMethod(M_DeleteById)
            .addParameter(ClassName.get(Serializable.class), "id");
        if (ifNotPrimary(builder, "no primary define found.")) {
            return builder.build();
        }
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("sql.DELETE_FROM($S)", fluent.getTableName())
            .addStatement("sql.WHERE($S)", fluent.getPrimary().getColumn() + " = #{id}")
            .addStatement("return sql.toString()");

        return builder.build();
    }

    private MethodSpec m_deleteByMap() {
        MethodSpec.Builder builder = super.sqlMethod(M_DeleteByMap).addParameter(Map_StrObj, Param_Map);
        builder.addStatement("Map<String, Object> cm = getParas(map, $S)", COLUMN_MAP);

        builder
            .addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("sql.DELETE_FROM($S)", fluent.getTableName())
            .addStatement("$T<String> where = new $T<>()", List.class, ArrayList.class)
            .addCode("for (String key : cm.keySet()) {\n")
            .addCode("\twhere.add(key + $S + key + $S);\n", " = #{cm.", "}")
            .addCode("}\n")
            .addStatement("sql.WHERE(where)")
            .addStatement("return sql.toString()");
        return builder.build();
    }

    private MethodSpec m_insert() {
        MethodSpec.Builder builder = super.sqlMethod(M_Insert).addParameter(fluent.className(), Param_Entity);

        builder
            .addStatement("assertNotNull($S, entity)", Param_Entity)
            .addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("sql.INSERT_INTO($S)", fluent.getTableName())
            .addStatement("$T<String> columns = new $T<>()", List.class, ArrayList.class)
            .addStatement("List<String> values = new ArrayList<>()");
        for (FieldColumn field : this.fluent.getFields()) {
            if (isNotBlank(field.getInsert())) {
                builder.addStatement("columns.add($S)", field.getColumn());
            }
            builder.addCode("if (entity.$L() != null) {\n", field.getMethodName());
            if (isBlank(field.getInsert())) {
                builder.addStatement("\tcolumns.add($S)", field.getColumn());
            }
            builder.addStatement("\tvalues.add($S)", field.getPropertyEl());
            if (isNotBlank(field.getInsert())) {
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
        MethodSpec.Builder builder = super.sqlMethod(M_InsertBatch).addParameter(ClassName.get(Map.class), Param_Map);

        builder.addStatement("assertNotEmpty($S, map)", Param_Map);
        String columns = this.fluent.getFields().stream()
            .map(FieldColumn::getColumn).map(c -> '"' + c + '"').collect(joining(", "));
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class)
            .addStatement("$T<$T> entities = getParas(map, $S)", List.class, fluent.className(), Param_List)
            .addStatement("sql.INSERT_INTO($S)", fluent.getTableName())
            .addStatement("sql.INSERT_COLUMNS($L)", columns)
            .addStatement("sql.VALUES()");

        String values = this.fluent.getFields().stream()
            .map(field -> {
                String variable = listIndexEl("list", field.getProperty(), "index");
                if (isNotBlank(field.getInsert())) {
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

    private void selectByWrapper(MethodSpec.Builder builder) {
        builder.addStatement("$T data = getWrapperData(map, $S)", WrapperData.class, WRAPPER);
        builder.addStatement("$T sql = new MapperSql()", MapperSql.class);
        builder.addStatement("sql.SELECT($S, data, $S)", fluent.getTableName(), fluent.getAllFields());
        builder.addStatement("sql.WHERE_GROUP_ORDER_BY(data)");
    }

    /**
     * 判断实例类是否有主键
     * 没有主键生成 "抛出异常语句"
     *
     * @param builder
     * @param error
     * @return
     */
    private boolean ifNotPrimary(MethodSpec.Builder builder, String error) {
        if (fluent.getPrimary() == null) {
            builder.addStatement("throw new $T($S)", ClassName.get(RuntimeException.class), error);
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