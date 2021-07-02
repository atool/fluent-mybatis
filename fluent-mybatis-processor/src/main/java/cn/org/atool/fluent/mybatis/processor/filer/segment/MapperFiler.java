package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseSqlProvider;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.mapper.FluentConst;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import javax.lang.model.element.Modifier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.*;

/**
 * 生成Entity对应的Mapper类
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public class MapperFiler extends AbstractFiler {

    private static final ClassName CN_Collection = ClassName.get(Collection.class);

    public MapperFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "Mapper接口";
    }

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Mapper;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Mapper);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        super.staticImport(spec);
        spec.addStaticImport(ClassName.get(FluentConst.class), "*");
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addSuperinterface(this.superMapperClass())
            .addSuperinterface(parameterizedType(ClassName.get(IRichMapper.class), fluent.entity()))
            .addSuperinterface(parameterizedType(ClassName.get(IWrapperMapper.class), fluent.entity()))
            .addSuperinterface(parameterizedType(ClassNames2.getClassName(fluent.getSuperMapper()), fluent.entity()))
            .addAnnotation(ClassNames2.Mybatis_Mapper)
            .addAnnotation(AnnotationSpec.builder(ClassNames2.Spring_Component)
                .addMember("value", "$S", getMapperName(this.fluent)).build()
            );
        spec.addField(FieldSpec.builder(String.class, "ResultMap",
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$S", fluent.getClassName() + "ResultMap")
            .build()
        );
        spec.addMethod(this.m_insert())
            .addMethod(this.m_insertWithPk())
            .addMethod(this.m_insertBatch())
            .addMethod(this.m_insertBatchWithPk())
            .addMethod(this.m_insertSelect())
            .addMethod(this.m_deleteById())
            .addMethod(this.m_logicDeleteById())
            .addMethod(this.m_deleteByIds())
            .addMethod(this.m_logicDeleteByIds())
            .addMethod(this.m_deleteByMap())
            .addMethod(this.m_logicDeleteByMap())
            .addMethod(this.m_delete())
            .addMethod(this.m_logicDelete())
            .addMethod(this.m_updateById())
            .addMethod(this.m_updateBy())
            .addMethod(this.m_findById())
            .addMethod(this.m_findOne())
            .addMethod(this.m_listByIds())
            .addMethod(this.m_listByMap())
            .addMethod(this.m_listEntity())
            .addMethod(this.m_listMaps())
            .addMethod(this.m_listObjs())
            .addMethod(this.m_count())
            .addMethod(this.m_countNoLimit());

        spec.addMethod(this.m_query())
            .addMethod(this.m_updater())
            .addMethod(this.m_defaultQuery())
            .addMethod(this.m_defaultUpdater())
            .addMethod(this.m_primaryField())
            .addMethod(this.m_entityClass());
    }

    private MethodSpec m_entityClass() {
        return super.publicMethod("entityClass", true, parameterizedType(ClassName.get(Class.class), fluent.entity()))
            .addModifiers(Modifier.DEFAULT)
            .addStatement("return $T.class", fluent.entity())
            .build();
    }

    private MethodSpec m_primaryField() {
        MethodSpec.Builder builder = super.publicMethod("primaryField", true, FieldMapping.class)
            .addModifiers(Modifier.DEFAULT);
        if (fluent.getPrimary() == null) {
            throwPrimaryNoFound(builder);
        } else {
            builder.addStatement("return $T.$L", fluent.mapping(), fluent.getPrimary().getName());
        }
        return builder.build();
    }

    private MethodSpec m_query() {
        return MethodSpec.methodBuilder(M_NEW_QUERY)
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(fluent.query())
            .addStatement("return new $T()", fluent.query())
            .build();
    }

    private MethodSpec m_defaultQuery() {
        return MethodSpec.methodBuilder(M_DEFAULT_QUERY)
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(fluent.query())
            .addStatement("return $T.INSTANCE.$L()", fluent.defaults(), M_DEFAULT_QUERY)
            .build();
    }

    private MethodSpec m_updater() {
        return MethodSpec.methodBuilder(M_NEW_UPDATER)
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(fluent.updater())
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    private MethodSpec m_defaultUpdater() {
        return MethodSpec.methodBuilder(M_DEFAULT_UPDATER)
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(fluent.updater())
            .addStatement("return $T.INSTANCE.$L()", fluent.defaults(), M_DEFAULT_UPDATER)
            .build();
    }

    public MethodSpec m_countNoLimit() {
        return this.mapperMethod(SelectProvider.class, M_countNoLimit)
            .addParameter(queryParam("query"))
            .returns(Integer.class)
            .build();
    }

    public MethodSpec m_count() {
        return this.mapperMethod(SelectProvider.class, M_count)
            .addParameter(queryParam("query"))
            .returns(Integer.class)
            .build();
    }

    public MethodSpec m_listObjs() {
        return this.mapperMethod(SelectProvider.class, M_listObjs)
            .addParameter(queryParam("query"))
            .returns(parameterizedType(ClassName.get(List.class), TypeVariableName.get("O")))
            .addTypeVariable(TypeVariableName.get("O"))
            .build();
    }

    public MethodSpec m_listMaps() {
        return this.mapperMethod(SelectProvider.class, M_listMaps)
            .addAnnotation(AnnotationSpec.builder(ResultType.class)
                .addMember("value", "$T.class", Map.class)
                .build())
            .addParameter(queryParam("query"))
            .returns(parameterizedType(ClassName.get(List.class), CN_Map_StrObj))
            .build();
    }

    public MethodSpec m_listEntity() {
        return this.mapperMethod(SelectProvider.class, M_listEntity)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(queryParam("query"))
            .returns(parameterizedType(ClassName.get(List.class), fluent.entity()))
            .build();
    }

    public MethodSpec m_listByMap() {
        return this.mapperMethod(SelectProvider.class, M_listByMap)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(this.param(CN_Map_StrObj, "columnMap", "Param_CM"))
            .returns(parameterizedType(ClassName.get(List.class), fluent.entity()))
            .build();
    }

    public MethodSpec m_listByIds() {
        return this.mapperMethod(SelectProvider.class, M_listByIds)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(this.param(Collection.class, "ids", "Param_List"))
            .returns(parameterizedType(CN_List, fluent.entity()))
            .build();
    }

    public MethodSpec m_findOne() {
        return this.mapperMethod(SelectProvider.class, M_findOne)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(queryParam("query"))
            .returns(fluent.entity())
            .build();
    }

    public MethodSpec m_findById() {
        return this.mapperMethod(SelectProvider.class, M_findById)
            .addAnnotation(this.annotation_Results())
            .addParameter(Serializable.class, "id")
            .returns(fluent.entity())
            .build();
    }

    public MethodSpec m_updateBy() {
        return this.mapperMethod(UpdateProvider.class, M_updateBy)
            .addParameter(this.param(ArrayTypeName.of(IUpdate.class), "updates", "Param_EW"))
            .addJavadoc(" {@link $T#updateBy($T)}", fluent.sqlProvider(), Map.class)
            .varargs(true)
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_updateById() {
        return this.mapperMethod(UpdateProvider.class, M_updateById)
            .addParameter(this.param(fluent.entity(), "entity", "Param_ET"))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_deleteByIds() {
        TypeName typeName = parameterizedType(ClassName.get(Collection.class), TypeVariableName.get("? extends Serializable"));
        return this.mapperMethod(DeleteProvider.class, M_deleteByIds)
            .addJavadoc("@see $T#deleteByIds(Map)", fluent.sqlProvider())
            .addParameter(this.param(typeName, "idList", "Param_List"))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_logicDeleteByIds() {
        TypeName typeName = parameterizedType(ClassName.get(Collection.class), TypeVariableName.get("? extends Serializable"));
        return this.mapperMethod(DeleteProvider.class, M_LogicDeleteByIds)
            .addJavadoc("@see $T#logicDeleteByIds(Map)", fluent.sqlProvider())
            .addParameter(this.param(typeName, "idList", "Param_List"))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_delete() {
        return this.mapperMethod(DeleteProvider.class, M_Delete)
            .addJavadoc("@see $T#delete(Map)", fluent.sqlProvider())
            .addParameter(queryParam("wrapper"))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_logicDelete() {
        return this.mapperMethod(DeleteProvider.class, M_LogicDelete)
            .addJavadoc("@see $T#logicDelete(Map)", fluent.sqlProvider())
            .addParameter(queryParam("wrapper"))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_deleteByMap() {
        return this.mapperMethod(DeleteProvider.class, M_DeleteByMap)
            .addJavadoc("@see $T#deleteByMap(Map)", fluent.sqlProvider())
            .addParameter(ParameterSpec.builder(CN_Map_StrObj, "cm")
                .addAnnotation(annotation_Param("Param_CM"))
                .build())
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_logicDeleteByMap() {
        return this.mapperMethod(DeleteProvider.class, M_LogicDeleteByMap)
            .addJavadoc("@see $T#logicDeleteByMap(Map)", fluent.sqlProvider())
            .addParameter(ParameterSpec.builder(CN_Map_StrObj, "cm")
                .addAnnotation(annotation_Param("Param_CM"))
                .build())
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_deleteById() {
        return this.mapperMethod(DeleteProvider.class, M_DeleteById)
            .addJavadoc("@see $T#deleteById(Serializable[])", fluent.sqlProvider())
            .addParameter(this.param(CN_SerializableArray, "ids", "Param_List"))
            .varargs(true)
            .returns(TypeName.INT)
            .build();
    }

    /**
     * 根据id逻辑删除
     *
     * @return MethodSpec
     */
    public MethodSpec m_logicDeleteById() {
        return this.mapperMethod(DeleteProvider.class, M_LogicDeleteById)
            .addJavadoc("@see $T#logicDeleteById(Serializable[])", fluent.sqlProvider())
            .addParameter(this.param(CN_SerializableArray, "ids", "Param_List"))
            .varargs(true)
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_insertBatch() {
        MethodSpec.Builder builder = this.mapperMethod(InsertProvider.class, M_InsertBatch);
        if (fluent.getPrimary() != null) {
            if (fluent.getPrimary().isAutoIncrease() && isBlank(fluent.getPrimary().getSeqName())) {
                this.addOptions(builder);
            } else {
                this.addSelectKey(builder);
            }
        }
        TypeName listType = parameterizedType(CN_Collection, fluent.entity());
        return builder.addParameter(this.param(listType, "entities", "Param_List"))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_insertBatchWithPk() {
        MethodSpec.Builder builder = this.mapperMethod(InsertProvider.class, M_InsertBatch_With_Pk);
        TypeName listType = parameterizedType(CN_Collection, fluent.entity());
        return builder.addParameter(this.param(listType, "entities", "Param_List"))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_insertSelect() {
        MethodSpec.Builder builder = this.mapperMethod(InsertProvider.class, M_InsertSelect);
        return builder
            .addJavadoc("@see $T#insertSelect(Map)", fluent.sqlProvider())
            .addParameter(this.param(String[].class, Param_Fields, "Param_Fields"))
            .addParameter(this.queryParam(Param_EW))
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_insert() {
        MethodSpec.Builder builder = this.mapperMethod(InsertProvider.class, M_Insert);
        if (fluent.getPrimary() != null) {
            if (fluent.getPrimary().isAutoIncrease() && isBlank(fluent.getPrimary().getSeqName())) {
                this.addOptions(builder);
            } else {
                this.addSelectKey(builder);
            }
        }
        return builder
            .addParameter(fluent.entity(), "entity")
            .returns(TypeName.INT)
            .addJavadoc("{@link $T#insert($T)}", BaseSqlProvider.class, IEntity.class)
            .build();
    }

    public MethodSpec m_insertWithPk() {
        MethodSpec.Builder builder = this.mapperMethod(InsertProvider.class, M_Insert_With_Pk);
        return builder
            .addParameter(fluent.entity(), "entity")
            .returns(TypeName.INT)
            .build();
    }

    private void addSelectKey(MethodSpec.Builder builder) {
        String seqName = fluent.getDbType().getSeq();
        boolean before = fluent.getDbType().isBefore();
        if (notBlank(fluent.getPrimary().getSeqName())) {
            seqName = fluent.getPrimary().getSeqName();
            before = fluent.getPrimary().isSeqIsBeforeOrder();
        }
        if (isBlank(seqName)) {
            return;
        }
        builder.addAnnotation(AnnotationSpec.builder(SelectKey.class)
            .addMember("resultType", "$T.class", fluent.getPrimary().getJavaType())
            .addMember("keyProperty", "$S", fluent.getPrimary().getName())
            .addMember("keyColumn", "$S", fluent.getPrimary().getColumn())
            .addMember("before", "$L", before)
            .addMember("statement", "$S", seqName)
            .build());
    }

    private void addOptions(MethodSpec.Builder builder) {
        builder.addAnnotation(AnnotationSpec.builder(Options.class)
            .addMember("useGeneratedKeys", "true")
            .addMember("keyProperty", "$S", fluent.getPrimary().getName())
            .addMember("keyColumn", "$S", fluent.getPrimary().getColumn())
            .build());
    }

    @Override
    protected boolean isInterface() {
        return true;
    }

    /**
     * 定义方式如下的方法
     * <pre>
     * public abstract Xyz methodName(...);
     * </pre>
     *
     * @param methodName 方法名称
     * @return MethodSpec
     */
    private MethodSpec.Builder mapperMethod(Class provider, String methodName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        builder.addAnnotation(Override.class);
        builder.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        builder.addAnnotation(AnnotationSpec.builder(provider)
            .addMember("type", "$T.class", fluent.sqlProvider())
            .addMember("method", "$S", methodName)
            .build());
        return builder;
    }

    private TypeName superMapperClass() {
        return super.parameterizedType(
            ClassName.get(IEntityMapper.class),
            fluent.entity()
        );
    }

    /**
     * 返回对应的Mapper Bean名称
     *
     * @param fluentEntity 实体原数据
     * @return mapper bean name
     */
    public static String getMapperName(FluentEntity fluentEntity) {
        String className = fluentEntity.getNoSuffix() + Suffix_Mapper;
        if (isBlank(fluentEntity.getMapperBeanPrefix())) {
            return MybatisUtil.lowerFirst(className, "");
        } else {
            return fluentEntity.getMapperBeanPrefix() + className;
        }
    }

    /**
     * <pre>
     *      @ResultMap("ResultMap")
     * </pre>
     *
     * @return AnnotationSpec
     */
    private AnnotationSpec annotation_ResultMap() {
        return AnnotationSpec.builder(ResultMap.class).addMember("value", "ResultMap").build();
    }

    /**
     * <pre>
     *      @Results(id="", value={@Result()}
     * </pre>
     *
     * @return AnnotationSpec
     */
    private AnnotationSpec annotation_Results() {
        List<CodeBlock> results = new ArrayList<>();
        for (CommonField field : fluent.getFields()) {
            List<CodeBlock> blocks = new ArrayList<>();
            blocks.add(CodeBlock.of("@$T(", Result.class));
            blocks.add(CodeBlock.of("column = $S", field.getColumn()));
            blocks.add(CodeBlock.of(", property = $S", field.getName()));
            blocks.add(CodeBlock.of(", javaType = $T.class", field.getJavaType()));
            if (field.isPrimary()) {
                blocks.add(CodeBlock.of(", id = true"));
            }
            if (field.getJdbcType() != null) {
                blocks.add(CodeBlock.of(", jdbcType = $T.$L", JdbcType.class, field.getJdbcType()));
            }
            if (field.getTypeHandler() != null) {
                blocks.add(CodeBlock.of(", typeHandler = $T.class", field.getTypeHandler()));
            }
            blocks.add(CodeBlock.of(")"));
            results.add(CodeBlock.join(blocks, ""));
        }
        return AnnotationSpec.builder(Results.class)
            .addMember("id", "ResultMap")
            .addMember("value", "{\n$L\n}", CodeBlock.join(results, ",\n"))
            .build();
    }

    /**
     * <pre>
     *      @Param("value") 注解
     * </pre>
     *
     * @param value 注解属性值
     * @return AnnotationSpec
     */
    private AnnotationSpec annotation_Param(String value) {
        return AnnotationSpec.builder(Param.class).addMember("value", "$L", value).build();
    }

    private ParameterSpec queryParam(String param_ew) {
        return this.param(IQuery.class, param_ew, "Param_EW");
    }

    /**
     * 声明  @Param("paraName") varName
     *
     * @param type     变量类型
     * @param varName  变量名称
     * @param paraName 注解名称
     * @return @Param("paraName") varName
     */
    private ParameterSpec param(Class type, String varName, String paraName) {
        return ParameterSpec.builder(type, varName)
            .addAnnotation(annotation_Param(paraName))
            .build();
    }

    private ParameterSpec param(TypeName type, String varName, String paraName) {
        return ParameterSpec.builder(type, varName)
            .addAnnotation(annotation_Param(paraName))
            .build();
    }
}