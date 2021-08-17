package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.InsertList;
import cn.org.atool.fluent.mybatis.base.model.UpdateDefault;
import cn.org.atool.fluent.mybatis.base.model.UpdateSet;
import cn.org.atool.fluent.mybatis.base.provider.BaseSqlProvider;
import cn.org.atool.fluent.mybatis.mapper.FluentConst;
import cn.org.atool.fluent.mybatis.mapper.MapperSql;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.PrimaryField;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.SqlProviderUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.DOUBLE_QUOTATION;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_SET_ENTITY_BY_DEFAULT;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.*;

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
    protected void staticImport(JavaFile.Builder spec) {
        super.staticImport(spec);
        spec.addStaticImport(MybatisUtil.class, "*");
        spec.addStaticImport(SqlProviderUtils.class, "*");
        spec.addStaticImport(FluentConst.class, "*");
        spec.addStaticImport(fluent.mapping(), "*");
        spec.addStaticImport(InsertList.class, "el");
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(parameterizedType(ClassName.get(BaseSqlProvider.class), fluent.entity()));
        spec.addField(this.f_defaults());
        // provider method
        spec.addMethod(this.m_primaryIsNull());
        spec.addMethod(this.m_primaryNotNull());
        spec.addMethod(this.m_insertEntity());
        spec.addMethod(this.m_insertBatchEntity());
        spec.addMethod(this.m_updateById());

        //Override method
        spec.addMethod(this.m_updateDefaults());
        spec.addMethod(this.m_tableName());
        spec.addMethod(this.m_mapping());
        spec.addMethod(this.m_allFields());
        spec.addMethod(this.m_setEntityByDefault());
        spec.addMethod(this.m_dbType());
        spec.addMethod(this.m_longTypeOfLogicDelete());
        PrimaryField pr = fluent.getPrimary();
        if (pr != null && notBlank(pr.getSeqName())) {
            spec.addMethod(this.m_getSeq(pr.getSeqName()));
        }
    }

    private MethodSpec m_getSeq(String seqName) {
        return super.protectedMethod("getSeq", true, ClassName.get(String.class))
            .addStatement("return $S", seqName)
            .build();
    }

    private MethodSpec m_longTypeOfLogicDelete() {
        return super.protectedMethod("longTypeOfLogicDelete", true, ClassName.BOOLEAN)
            .addStatement("return $L", fluent.isLongTypeOfLogicDelete())
            .build();
    }

    private MethodSpec m_setEntityByDefault() {
        return super.protectedMethod(M_SET_ENTITY_BY_DEFAULT, true, null)
            .addParameter(IEntity.class, "entity")
            .addStatement("defaults.setEntityByDefault(entity)")
            .build();
    }

    private MethodSpec m_updateDefaults() {
        MethodSpec.Builder builder = super.publicMethod("updateDefaults", true, CN_List_Str)
            .addParameter(CN_Map_StrStr, "updates")
            .addParameter(ClassName.BOOLEAN, "ignoreLockVersion");
        builder.addStatement("UpdateDefault defaults = new $T(updates)", UpdateDefault.class);

        for (CommonField field : this.fluent.getFields()) {
            if (isBlank(field.getUpdate())) {
                continue;
            }
            if (Objects.equals(field.getName(), fluent.getVersionField())) {
                builder.addCode("if (!ignoreLockVersion) {\n");
                builder.addStatement("\tdefaults.add(dbType(), $L, $S)", field.getName(), field.getUpdate());
                builder.addCode("}\n");
            } else {
                builder.addStatement("defaults.add(dbType(), $L, $S)", field.getName(), field.getUpdate());
            }
        }
        return builder.addStatement("return defaults.getUpdateDefaults()").build();
    }

    private MethodSpec m_updateById() {
        MethodSpec.Builder spec = super.publicMethod(M_updateById, false, String.class)
            .addParameter(CN_Map_StrObj, Param_Map);
        if (this.ifNotPrimary(spec)) {
            return spec.build();
        }
        spec.addStatement("$T entity = getParas(map, Param_ET)", fluent.entity());
        spec.addStatement("assertNotNull(Param_Entity, entity)");
        spec.addStatement("$T sql = new MapperSql()", MapperSql.class);
        spec.addStatement("sql.UPDATE(this.tableName())");
        spec.addCode("$T updates = new UpdateSet()", UpdateSet.class);

        CommonField versionField = null;
        for (CommonField field : this.fluent.getFields()) {
            if (Objects.equals(field.getName(), fluent.getVersionField())) {
                spec.addCode("\n\t.add(this.dbType(), $L, null, $S)", field.getName(), field.getUpdate());
                versionField = field;
            } else if (!field.isPrimary()) {
                spec.addCode("\n\t.add(this.dbType(), $L, entity.$L(), $S)", field.getName(), field.getMethodName(), field.getUpdate());
            }
        }
        spec.addCode(";\n");
        if (versionField != null) {
            spec.addStatement("assertNotNull($S, entity.$L())",
                String.format("lock version field(%s)", versionField.getName()),
                versionField.getMethodName());
        }
        spec.addStatement("sql.SET(updates.getUpdates())");
        spec.addStatement("sql.WHERE($L.el(this.dbType(), Param_ET))", fluent.getPrimary().getName());
        if (versionField != null) {
            spec.addStatement("sql.APPEND($S)", " AND ");
            spec.addStatement("sql.APPEND($L.el(this.dbType(), Param_ET))", versionField.getName());
        }
        return spec.addStatement("return sql.toString()").build();
    }

    private MethodSpec m_primaryIsNull() {
        MethodSpec.Builder spec = super.publicMethod("primaryIsNull", true, ClassName.BOOLEAN)
            .addParameter(fluent.entity(), Param_Entity);
        if (fluent.getPrimary() == null) {
            spec.addStatement("return true");
        } else {
            spec.addStatement("return entity.$L() == null", fluent.getPrimary().getMethodName());
        }
        return spec.build();
    }

    private MethodSpec m_primaryNotNull() {
        MethodSpec.Builder spec = super.publicMethod("primaryNotNull", true, ClassName.BOOLEAN)
            .addParameter(fluent.entity(), Param_Entity);
        if (fluent.getPrimary() == null) {
            spec.addStatement("return true");
        } else {
            spec.addStatement("return entity.$L() != null", fluent.getPrimary().getMethodName());
        }
        return spec.build();
    }

    private MethodSpec m_insertEntity() {
        MethodSpec.Builder spec = super.protectedMethod(M_Insert_Entity, true, null)
            .addParameter(InsertList.class, "inserts")
            .addParameter(String.class, "prefix")
            .addParameter(fluent.entity(), Param_Entity)
            .addParameter(ClassName.BOOLEAN, "withPk");

        for (CommonField field : this.fluent.getFields()) {
            if (field.isPrimary()) {
                spec.addCode("if (withPk) {\n")
                    .addStatement("\tinserts.add(prefix, $L, entity.$L(), $S)", field.getName(), field.getMethodName(), field.getInsert())
                    .addCode("}\n");
            } else {
                spec.addStatement("inserts.add(prefix, $L, entity.$L(), $S)", field.getName(), field.getMethodName(), field.getInsert());
            }
        }
        return spec.build();
    }

    private MethodSpec m_insertBatchEntity() {
        MethodSpec.Builder spec = super.protectedMethod(M_Insert_Batch_Entity, true, CN_List_Str)
            .addParameter(ClassName.INT, "index")
            .addParameter(fluent.entity(), Param_Entity)
            .addParameter(ClassName.BOOLEAN, "withPk")
            .addStatement("$T<String> values = new $T<>()", List.class, ArrayList.class);

        for (CommonField field : this.fluent.getFields()) {
            if (field.isPrimary()) {
                spec.addCode("if (withPk) {\n")
                    .addStatement("\tvalues.add(el($S + index + $S, $L, entity.$L(), $S))",
                        "list[", "].", field.getName(), field.getMethodName(), field.getInsert())
                    .addCode("}\n");
            } else {
                spec.addStatement("values.add(el($S + index + $S, $L, entity.$L(), $S))",
                    "list[", "].", field.getName(), field.getMethodName(), field.getInsert());
            }
        }
        spec.addStatement("return values");
        return spec.build();
    }

    private MethodSpec m_tableName() {
        return super.publicMethod("tableName", true, String.class)
            .addStatement("return defaults.table().get()")
            .build();
    }

    private MethodSpec m_allFields() {
        MethodSpec.Builder spec = super.publicMethod("allFields", true, ClassNames2.CN_List_Str)
            .addParameter(ClassName.BOOLEAN, "withPk");
        spec.addCode("if (withPk) {\n")
            .addStatement("\treturn $T.asList($L)", Arrays.class, this.getFields(true))
            .addCode("} else {\n")
            .addStatement("\treturn $T.asList($L)", Arrays.class, this.getFields(false))
            .addCode("}");
        return spec.build();
    }

    private String getFields(boolean withPk) {
        StringBuilder fields = new StringBuilder();
        boolean first = true;
        for (CommonField field : fluent.getFields()) {
            if (field.isPrimary() && !withPk) {
                continue;
            }
            if (!first) {
                fields.append(", ");
            }
            first = false;
            fields.append(DOUBLE_QUOTATION).append(field.getColumn()).append(DOUBLE_QUOTATION);
        }
        return fields.toString();
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
     * @param builder MethodSpec.Builder
     * @return true: 有主键
     */
    private boolean ifNotPrimary(MethodSpec.Builder builder) {
        if (fluent.getPrimary() == null) {
            throwPrimaryNoFound(builder);
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