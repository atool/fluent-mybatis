package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IEntityKit;
import cn.org.atool.fluent.mybatis.base.model.EntityToMap;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.UniqueFieldType;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Helper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_EntityMapping;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.DOUBLE_QUOTATION;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NEWLINE;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_List_FMapping;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Map_StrObj;
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
        spec.addStaticImport(Optional.class, "ofNullable");
        spec.addStaticImport(UniqueFieldType.class, "*");
        spec.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(paraType(ClassName.get(AMapping.class), fluent.entity(), fluent.query(), fluent.updater()))
            .addSuperinterface(IEntityKit.class);
        spec.addField(this.f_Table_Name())
            .addField(this.f_Entity_Name())
            .addFields(this.f_Fields())
            .addField(this.f_allFieldMapping())
            /* 放在所有静态变量后面 */
            .addField(this.f_instance())
            .addField(this.f_setter());

        spec.addMethod(this.m_constructor())
            .addMethod(this.m_column2mapping())
            .addMethod(this.m_field2mapping())
            .addMethod(this.m_entityClass())
            .addMethod(this.m_emptyQuery())
            .addMethod(this.m_emptyUpdater())
            .addMethod(this.m_aliasQuery_2())
            .addMethod(this.m_setter());
        spec.addMethod(this.m_toColumnMap())
            .addMethod(this.m_toEntityMap())
            .addMethod(this.m_toMap())
            .addMethod(this.m_toEntity())
            .addMethod(this.m_copy())
            .addMethod(this.m_getFields())
            .addMethod(this.m_getFieldValue());
    }

    private FieldSpec f_Table_Name() {
        return FieldSpec.builder(String.class, "Table_Name", Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
            .initializer("$S", fluent.getTableName())
            .addJavadoc(super.codeBlock("表名称"))
            .build();
    }

    private FieldSpec f_Entity_Name() {
        return FieldSpec.builder(String.class, "Entity_Name", Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
            .initializer("$S", fluent.getClassName())
            .addJavadoc(super.codeBlock("Entity名称"))
            .build();
    }

    private FieldSpec f_instance() {
        return FieldSpec.builder(fluent.entityKit(), "MAPPING", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .initializer("new $T()", fluent.entityKit())
            .build();
    }

    private FieldSpec f_allFieldMapping() {
        String fields = fluent.getFields().stream()
            .map(f -> f.getName())
            .collect(joining(", "));
        return FieldSpec.builder(CN_List_FMapping, "ALL_FIELD_MAPPING", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .initializer("$T.asList($L)", Arrays.class, fields)
            .build();
    }

    private List<FieldSpec> f_Fields() {
        List<FieldSpec> fields = new ArrayList<>();
        for (CommonField f : fluent.getFields()) {
            String name = f.getName();
            FieldSpec.Builder spec = FieldSpec.builder(FieldMapping.class,
                f.getName(), Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("实体属性 : 数据库字段 映射\n $L : $L", name, f.getColumn());
            UniqueFieldType type = null;
            if (f.isPrimary()) {
                type = UniqueFieldType.PRIMARY_ID;
            } else if (Objects.equals(name, fluent.getVersionField())) {
                type = UniqueFieldType.LOCK_VERSION;
            } else if (Objects.equals(name, fluent.getLogicDelete())) {
                type = UniqueFieldType.LOGIC_DELETED;
            }
            String prev5para = quota(name) + ", " +
                quota(f.getColumn()) + ", " +
                (type == null ? "null" : type.name()) + ", " +
                quota(f.getInsert()) + ", " +
                quota(f.getUpdate());

            if (f.getTypeHandler() == null) {
                spec.initializer("new FieldMapping($L, $T.class, null)", prev5para, f.getJavaType());
            } else {
                spec.initializer("new FieldMapping($L, $T.class, $T.class)", prev5para, f.getJavaType(), f.getTypeHandler());
            }
            fields.add(spec.build());
        }
        return fields;
    }

    private FieldSpec f_setter() {
        ClassName type = ClassNames2.getClassName(fluent.getDefaults());
        return FieldSpec.builder(type, "CustomizedSetter")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.FINAL)
            .initializer("new $T(){}", type)
            .build();
    }

    /* ===== METHOD ==== */
    private MethodSpec m_toColumnMap() {
        return super.publicMethod("toColumnMap", true, CN_Map_StrObj)
            .addParameter(IEntity.class, "entity")
            .addParameter(ClassName.BOOLEAN, "isNoN")
            .addStatement("return this.toMap(($T)entity, false, isNoN)", fluent.entity())
            .build();
    }

    private MethodSpec m_toEntityMap() {
        return super.publicMethod("toEntityMap", true, CN_Map_StrObj)
            .addParameter(IEntity.class, "entity")
            .addParameter(ClassName.BOOLEAN, "isNoN")
            .addStatement("return this.toMap(($T)entity, true, isNoN)", fluent.entity())
            .build();
    }

    /**
     * public static Map<String, Object> toEntityMap(Entity entity)
     *
     * @return MethodSpec
     */
    private MethodSpec m_toMap() {
        MethodSpec.Builder builder = super.publicMethod("toMap", false, CN_Map_StrObj)
            .addParameter(fluent.entity(), "entity")
            .addParameter(TypeName.BOOLEAN, "isProperty")
            .addParameter(ClassName.BOOLEAN, "isNoN")
            .addCode("return new $T(isProperty)\n", EntityToMap.class);
        for (CommonField fc : fluent.getFields()) {
            String getMethod = fc.getMethodName();
            builder.addCode("\t.put($L, entity.$L(), isNoN)\n", fc.getName(), getMethod);
        }
        return builder.addCode("\t.getMap();").build();
    }

    /**
     * public Entity entity(Map<String, Object> map)
     *
     * @return MethodSpec
     */
    private MethodSpec m_toEntity() {
        MethodSpec.Builder builder = super.publicMethod("toEntity", true, TypeVariableName.get("E"))
            .addParameter(this.mapType(String.class, Object.class), "map")
            .addTypeVariable(TypeVariableName.get("E", IEntity.class))
            .addStatement("$T entity = new $T()", fluent.entity(), fluent.entity());
        for (CommonField fc : fluent.getFields()) {
            String setMethod = fc.setMethodName();

            builder.addCode("if (map.containsKey($L.name)) {\n", fc.getName());
            builder.addCode("\tentity.$L(($T) map.get($L.name));\n",
                setMethod, fc.getJavaType(), fc.getName());
            builder.addCode("}\n");
        }
        return builder.addStatement("return (E)entity").build();
    }

    /**
     * public static Entity copy(Entity entity)
     *
     * @return MethodSpec
     */
    private MethodSpec m_copy() {
        MethodSpec.Builder builder = super.publicMethod("copy", true, fluent.entity())
            .addParameter(IEntity.class, "iEntity")
            .addStatement("$T entity = ($T) iEntity", fluent.entity(), fluent.entity())
            .addStatement("$T copy = new $T()", fluent.entity(), fluent.entity());
        builder.addCode("{\n");
        for (CommonField fc : fluent.getFields()) {
            builder.addStatement("\tcopy.$L(entity.$L())", fc.setMethodName(), fc.getMethodName());
        }
        builder.addCode("}\n");
        return builder.addStatement("return copy").build();
    }

    private MethodSpec m_getFields() {
        MethodSpec.Builder spec = super.publicMethod("getFields", true, CN_List_FMapping);
        spec.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addStatement("return ALL_FIELD_MAPPING");
        return spec.build();
    }

    private MethodSpec m_getFieldValue() {
        MethodSpec.Builder spec = super.publicMethod("getFieldValue", Object.class);
        spec.addParameter(IEntity.class, "entity")
            .addParameter(String.class, "prop")
            .beginControlFlow("if (!(entity instanceof $T) || prop == null)", fluent.entity())
            .addStatement("return null")
            .endControlFlow()
            .addStatement("$T e = ($T) entity", fluent.entity(), fluent.entity())
            .addCode("switch (prop) {\n");
        for (CommonField field : fluent.getFields()) {
            spec.addCode("\tcase $S: return e.$L();\n", field.getName(), field.getMethodName());
        }
        return spec
            .addCode("\tdefault: return null;\n")
            .addCode("}")
            .build();
    }

    private MethodSpec m_constructor() {
        MethodSpec.Builder spec = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PROTECTED)
            .addStatement("super($T.$L)", DbType.class, fluent.getDbType().name())
            .addStatement("super.tableName = Table_Name");
        this.putUniqueField(spec);
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

    private MethodSpec m_column2mapping() {
        String statement = this.fluent.getFields().stream()
            .map(CommonField::getName)
            .map(field -> String.format("map.put(%s.column, %s);", field, field))
            .collect(joining(NEWLINE));

        return MethodSpec.methodBuilder("column2mapping")
            .returns(ParameterizedTypeName.get(Map.class, String.class, FieldMapping.class))
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("数据库字段对应的FieldMapping")
            .addStatement("$T<String, FieldMapping> map = new $T<>(32)", Map.class, LinkedHashMap.class)
            .addCode(statement + "\n")
            .addStatement("return $T.unmodifiableMap(map)", Collections.class)
            .build();
    }

    private MethodSpec m_field2mapping() {
        String statement = this.fluent.getFields().stream()
            .map(CommonField::getName)
            .map(field -> String.format("map.put(%s.name, %s);", field, field))
            .collect(joining(NEWLINE));

        return MethodSpec.methodBuilder("field2mapping")
            .returns(ParameterizedTypeName.get(Map.class, String.class, FieldMapping.class))
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("实体类属性对应的FieldMapping")
            .addStatement("$T<String, FieldMapping> map = new $T<>(32)", Map.class, LinkedHashMap.class)
            .addCode(statement + "\n")
            .addStatement("return $T.unmodifiableMap(map)", Collections.class)
            .build();
    }

    private MethodSpec m_entityClass() {
        return super.publicMethod("entityClass", Class.class)
            .addStatement("return $T.class", fluent.entity())
            .build();
    }

    private MethodSpec m_emptyQuery() {
        return super.publicMethod(M_NEW_QUERY, true, fluent.query())
            .addStatement("return new $T()", fluent.query()).build();
    }

    private MethodSpec m_emptyUpdater() {
        return super.publicMethod(M_NEW_UPDATER, true, fluent.updater())
            .addStatement("return new $T()", fluent.updater()).build();
    }

    private MethodSpec m_aliasQuery_2() {
        return super.publicMethod(M_ALIAS_QUERY, true, fluent.query())
            .addJavadoc(JavaDoc_Alias_Query_0)
            .addParameter(String.class, "alias")
            .addParameter(Parameters.class, "parameters")
            .addStatement("return new $T(alias, parameters)", fluent.query())
            .build();
    }

    private MethodSpec m_setter() {
        return super.publicMethod("setter", IDefaultSetter.class)
            .addStatement("return CustomizedSetter").build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    private String quota(String input) {
        if (isBlank(input)) {
            return null;
        } else {
            return DOUBLE_QUOTATION + input + DOUBLE_QUOTATION;
        }
    }
}