package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.TableId;
import cn.org.atool.fluent.mybatis.base.model.UniqueType;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.EntityRefMethod;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.PrimaryField;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import cn.org.atool.fluent.mybatis.processor.filer.FilerKit;
import cn.org.atool.fluent.mybatis.segment.fragment.Fragments;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.DOUBLE_QUOTATION;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_List_FMapping;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.FN_FieldMapping;
import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.PUBLIC_FINAL;
import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.PUBLIC_STATIC_FINAL;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;
import static java.util.stream.Collectors.joining;

/**
 * EntityHelper类代码生成
 *
 * @author wudarui
 */
public class EntityMappingFiler extends AbstractFiler {
    public static String getClassName(FluentClassName fluent) {
        return fluent.getNoSuffix() + Suffix_EntityMapping;
    }

    public static String getPackageName(FluentClassName fluent) {
        return fluent.getPackageName(Pack_Helper);
    }

    public EntityMappingFiler(FluentEntity fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = getClassName(fluent);
        this.comment = "Entity帮助类";
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(UniqueType.class, "*");
        spec.addStaticImport(Fragments.class, "fragment");
        spec.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(paraType(ClassName.get(AMapping.class), fluent.entity(), fluent.query(), fluent.updater()))
            .addField(this.f_Table_Name())
            .addField(this.f_Entity_Name());

        for (CommonField f : fluent.getFields()) {
            spec.addField(this.f_Field(f));
        }
        spec.addField(this.f_defaultSetter())
            .addField(this.f_allFieldMapping())
            /* 放在所有静态变量后面 */
            .addField(this.f_instance());

        spec.addMethod(this.m_constructor())
            .addMethod(this.m_entityClass())
            .addMethod(this.m_mapperClass())
            .addMethod(this.m_newEntity())
            .addMethod(this.m_allFields())
            .addMethod(this.m_defaultSetter())
            .addMethod(this.m_newQuery())
            .addMethod(this.m_newUpdater());
    }

    private FieldSpec f_Table_Name() {
        return FieldSpec.builder(String.class, "Table_Name", PUBLIC_STATIC_FINAL)
            .initializer("$S", fluent.getTableName())
            .addJavadoc(super.codeBlock("表名称"))
            .build();
    }

    private FieldSpec f_Entity_Name() {
        return FieldSpec.builder(String.class, "Entity_Name", PUBLIC_STATIC_FINAL)
            .initializer("$S", fluent.getClassName())
            .addJavadoc(super.codeBlock("Entity名称"))
            .build();
    }

    private FieldSpec f_instance() {
        return FieldSpec.builder(fluent.entityMapping(), Suffix_MAPPING, PUBLIC_STATIC_FINAL)
            .initializer("new $T()", fluent.entityMapping())
            .build();
    }

    private FieldSpec f_allFieldMapping() {
        String fields = fluent.getFields().stream()
            .map(CommonField::getName)
            .collect(joining(", "));
        return FieldSpec.builder(CN_List_FMapping, "ALL_FIELD_MAPPING", PUBLIC_STATIC_FINAL)
            .initializer("$T.unmodifiableList($T\n\t.asList($L))", Collections.class, Arrays.class, fields)
            .build();
    }

    private FieldSpec f_Field(CommonField f) {
        String name = f.getName();
        FieldSpec.Builder spec = FieldSpec
            .builder(ParameterizedTypeName.get(FN_FieldMapping, fluent.entity()), f.getName(), PUBLIC_STATIC_FINAL)
            .addJavadoc("实体属性 : 数据库字段 映射\n $L : $L", name, f.getColumn());
        UniqueType type = this.getUniqueType(f);
        CodeBlock.Builder init = CodeBlock.builder();
        init.add("new FieldMapping<$T>", fluent.entity());
        init.add("\n\t(")
            .add("$L, $L, ", quota(name), quota(f.getColumn()))
            .add(type == null ? "null" : type.name())
            .add(", $L, $L, ", quota(f.getInsert()), quota(f.getUpdate()))
            .add("$T.class, ", f.getJavaType());

        if (f.getTypeHandler() == null) {
            init.add("null)");
        } else {
            init.add("$T.class)", f.getTypeHandler());
        }
        init.add("\n\t.sg((e, v) -> e.$L(($T) v), $T::$L)",
            f.setMethodName(), f.getJavaType(), fluent.entity(), f.getMethodName());
        return spec.initializer(init.build()).build();
    }

    private UniqueType getUniqueType(CommonField field) {
        if (field.isPrimary()) {
            return UniqueType.PRIMARY_ID;
        } else if (Objects.equals(field.getName(), fluent.getVersionField())) {
            return UniqueType.LOCK_VERSION;
        } else if (Objects.equals(field.getName(), fluent.getLogicDelete())) {
            return UniqueType.LOGIC_DELETED;
        } else {
            return null;
        }
    }

    private FieldSpec f_defaultSetter() {
        ClassName type = ClassNames2.getClassName(fluent.getDefaults());
        return FieldSpec.builder(type, "DEFAULT_SETTER")
            .addModifiers(PUBLIC_STATIC_FINAL)
            .initializer("new $T(){}", type)
            .build();
    }

    /* ===== METHOD ==== */

    private MethodSpec m_constructor() {
        MethodSpec.Builder spec = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PROTECTED)
            .addStatement("super($T.$L)", DbType.class, fluent.getDbType().name())
            .addStatement("super.tableName = Table_Name");
        PrimaryField p = fluent.getPrimary();
        if (p != null) {
            spec.addStatement("super.tableId = new $T($S, $S, $L, $S, $L)",
                TableId.class, p.getName(), p.getColumn(), p.isAutoIncrease(), p.getSeqName(), p.isSeqIsBeforeOrder());
        }
        this.putUniqueField(spec);
        this.addRef(spec);
        return spec.build();
    }

    private void addRef(MethodSpec.Builder spec) {
        String del = "\";\"";
        for (EntityRefMethod m : fluent.getRefMethods()) {
            if (m.getMapping().isEmpty()) {
                continue;
            }
            StringBuilder src = new StringBuilder(del);
            StringBuilder ref = new StringBuilder(del);
            for (Map.Entry<String, String> entry : m.getMapping().entrySet()) {
                src.append(" + e.get").append(capitalFirst(entry.getValue(), "")).append("() + ").append(del);
                ref.append(" + e.get").append(capitalFirst(entry.getKey(), "")).append("() + ").append(del);
            }
            spec.addStatement("super.ref($S, e -> $L, $L, ($T e) -> $L)",
                m.getName(), src, m.returnList(), m.getReturnType(), ref);
        }
        spec.addStatement("super.Ref_Keys.unmodified()");
    }

    /**
     * public Entity newEntity()
     *
     * @return MethodSpec
     */
    private MethodSpec m_newEntity() {
        return FilerKit.publicMethod("newEntity", TypeVariableName.get("E"))
            .addTypeVariable(TypeVariableName.get("E", IEntity.class))
            .addStatement("return (E) new $T()", fluent.entity())
            .build();
    }

    private MethodSpec m_allFields() {
        MethodSpec.Builder spec = FilerKit.publicMethod("allFields", CN_List_FMapping);
        spec.addModifiers(PUBLIC_FINAL)
            .addStatement("return ALL_FIELD_MAPPING");
        return spec.build();
    }

    private void putUniqueField(MethodSpec.Builder spec) {
        if (fluent.getPrimary() != null) {
            spec.addStatement("super.uniqueFields.put(PRIMARY_ID, $L)", fluent.getPrimary().getName());
        }
        if (notBlank(fluent.getLogicDelete())) {
            spec.addStatement("super.uniqueFields.put(LOGIC_DELETED, $L)", fluent.getLogicDelete());
        }
        if (notBlank(fluent.getVersionField())) {
            spec.addStatement("super.uniqueFields.put(LOCK_VERSION, $L)", fluent.getVersionField());
        }
    }

    private MethodSpec m_entityClass() {
        return FilerKit.publicMethod(F_Entity_Class, Class.class)
            .addStatement("return $T.class", fluent.entity())
            .build();
    }

    private MethodSpec m_mapperClass() {
        return FilerKit.publicMethod("mapperClass", Class.class)
            .addStatement("return $T.class", fluent.mapper())
            .build();
    }

    private MethodSpec m_newQuery() {
        return FilerKit.protectMethod("query", fluent.query())
            .addModifiers(Modifier.FINAL)
            .addParameter(boolean.class, "defaults")
            .addParameter(StringSupplier.class, "table")
            .addParameter(StringSupplier.class, "alias")
            .addParameter(Parameters.class, "shared")
            .addStatement("return new $T(defaults, fragment(table), alias, shared)", fluent.query())
            .build();
    }

    private MethodSpec m_newUpdater() {
        return FilerKit.protectMethod("updater", fluent.updater())
            .addModifiers(Modifier.FINAL)
            .addParameter(boolean.class, "defaults")
            .addParameter(StringSupplier.class, "table")
            .addParameter(StringSupplier.class, "alias")
            .addParameter(Parameters.class, "shared")
            .addStatement("return new $T(defaults, fragment(table), alias, shared)", fluent.updater())
            .build();
    }

    private MethodSpec m_defaultSetter() {
        return FilerKit.publicMethod("defaultSetter", IDefaultSetter.class)
            .addStatement("return DEFAULT_SETTER").build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    private String quota(String input) {
        return isBlank(input) ? null : DOUBLE_QUOTATION + input + DOUBLE_QUOTATION;
    }
}