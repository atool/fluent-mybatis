package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IUpdate;
import cn.org.atool.fluent.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.ClassNames;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.mapper.FluentConst;
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
import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Map_StrObj;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * 生成Entity对应的Mapper类
 *
 * @author darui.wu
 */
public class MapperGenerator extends AbstractGenerator {

    public MapperGenerator(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "Mapper接口";
    }

    public static String getClassName(FluentEntity fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Mapper;
    }

    public static String getPackageName(FluentEntity fluentEntity) {
        return fluentEntity.getPackageName(Pack_Mapper);
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        super.staticImport(builder);
        builder.addStaticImport(ClassName.get(FluentConst.class), "*");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addSuperinterface(this.superMapperClass()).addAnnotation(ClassNames.CN_Mapper);
        builder.addAnnotation(AnnotationSpec.builder(ClassNames.CN_Component)
            .addMember("value", "$S", getMapperName(this.fluent)).build()
        );
        builder.addField(FieldSpec.builder(String.class, "ResultMap",
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$S", fluent.getClassName() + "ResultMap")
            .build()
        );
        builder.addMethod(this.m_insert());
        builder.addMethod(this.m_insertBatch());
        builder.addMethod(this.m_deleteById());
        builder.addMethod(this.m_deleteByMap());
        builder.addMethod(this.m_delete());
        builder.addMethod(this.m_deleteByIds());
        builder.addMethod(this.m_updateById());
        builder.addMethod(this.m_updateBy());
        builder.addMethod(this.m_findById());
        builder.addMethod(this.m_findOne());
        builder.addMethod(this.m_listByIds());
        builder.addMethod(this.m_listByMap());
        builder.addMethod(this.m_listEntity());
        builder.addMethod(this.m_listMaps());
        builder.addMethod(this.m_listObjs());
        builder.addMethod(this.m_count());
        builder.addMethod(this.m_countNoLimit());

        builder.addMethod(this.m_query());
        builder.addMethod(this.m_updater());
    }

    private MethodSpec m_updater() {
        return MethodSpec.methodBuilder("updater")
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(fluent.updater())
            .addJavadoc("更新条件设置\n\n@return")
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    private MethodSpec m_query() {
        return MethodSpec.methodBuilder("query")
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(fluent.query())
            .addJavadoc("查询条件设置\n\n@return")
            .addStatement("return new $T()", fluent.query())
            .build();
    }

    public MethodSpec m_countNoLimit() {
        return this.mapperMethod(SelectProvider.class, M_countNoLimit)
            .addParameter(ParameterSpec.builder(IQuery.class, "query")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
            .returns(Integer.class)
            .build();
    }

    public MethodSpec m_count() {
        return this.mapperMethod(SelectProvider.class, M_count)
            .addParameter(ParameterSpec.builder(IQuery.class, "query")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
            .returns(Integer.class)
            .build();
    }

    public MethodSpec m_listObjs() {
        return this.mapperMethod(SelectProvider.class, M_listObjs)
            .addParameter(ParameterSpec.builder(IQuery.class, "query")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
            .returns(parameterizedType(ClassName.get(List.class), TypeVariableName.get("O")))
            .addTypeVariable(TypeVariableName.get("O"))
            .build();
    }

    public MethodSpec m_listMaps() {
        return this.mapperMethod(SelectProvider.class, M_listMaps)
            .addAnnotation(AnnotationSpec.builder(ResultType.class)
                .addMember("value", "$T.class", Map.class)
                .build())
            .addParameter(ParameterSpec.builder(IQuery.class, "query")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
            .returns(parameterizedType(ClassName.get(List.class), CN_Map_StrObj))
            .build();
    }

    public MethodSpec m_listEntity() {
        return this.mapperMethod(SelectProvider.class, M_listEntity)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(ParameterSpec.builder(IQuery.class, "query")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
            .returns(parameterizedType(ClassName.get(List.class), fluent.entity()))
            .build();
    }

    public MethodSpec m_listByMap() {
        return this.mapperMethod(SelectProvider.class, M_listByMap)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(ParameterSpec.builder(CN_Map_StrObj, "columnMap")
                .addAnnotation(annotation_Param("Param_CM"))
                .build())
            .returns(parameterizedType(ClassName.get(List.class), fluent.entity()))
            .build();
    }

    public MethodSpec m_listByIds() {
        return this.mapperMethod(SelectProvider.class, M_listByIds)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(ParameterSpec.builder(Collection.class, "ids")
                .addAnnotation(annotation_Param("Param_Coll"))
                .build())
            .returns(parameterizedType(ClassName.get(List.class), fluent.entity()))
            .build();
    }

    public MethodSpec m_findOne() {
        return this.mapperMethod(SelectProvider.class, M_findOne)
            .addAnnotation(this.annotation_ResultMap())
            .addParameter(ParameterSpec.builder(IQuery.class, "query")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
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
            .addParameter(ParameterSpec.builder(IUpdate.class, "update")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_updateById() {
        return this.mapperMethod(UpdateProvider.class, M_updateById)
            .addParameter(ParameterSpec.builder(fluent.entity(), "entity")
                .addAnnotation(annotation_Param("Param_ET"))
                .build())
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_deleteByIds() {
        return this.mapperMethod(DeleteProvider.class, M_deleteByIds)
            .addParameter(ParameterSpec.builder(parameterizedType(ClassName.get(Collection.class), TypeVariableName.get("? extends Serializable")), "idList")
                .addAnnotation(annotation_Param("Param_Coll"))
                .build())
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_delete() {
        return this.mapperMethod(DeleteProvider.class, M_Delete)
            .addParameter(ParameterSpec.builder(IQuery.class, "wrapper")
                .addAnnotation(annotation_Param("Param_EW"))
                .build())
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_deleteByMap() {
        return this.mapperMethod(DeleteProvider.class, M_DeleteByMap)
            .addParameter(ParameterSpec.builder(CN_Map_StrObj, "cm")
                .addAnnotation(annotation_Param("Param_CM"))
                .build())
            .returns(TypeName.INT)
            .build();
    }


    public MethodSpec m_deleteById() {
        return this.mapperMethod(DeleteProvider.class, M_DeleteById)
            .addParameter(ClassName.get(Serializable.class), "id")
            .returns(TypeName.INT)
            .build();
    }

    public MethodSpec m_insertBatch() {
        return this.mapperMethod(InsertProvider.class, M_InsertBatch)
            .addParameter(parameterizedType(ClassName.get(List.class), fluent.entity()), "entities")
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
            .build();
    }

    private void addSelectKey(MethodSpec.Builder builder) {
        String seqName = fluent.getDbType().getSeq();
        boolean before = fluent.getDbType().isBefore();
        if (notBlank(fluent.getPrimary().getSeqName())) {
            seqName = fluent.getPrimary().getSeqName();
            before = fluent.getPrimary().isSeqIsBeforeOrder();
        }
        builder.addAnnotation(AnnotationSpec.builder(SelectKey.class)
            .addMember("resultType", "$T.class", fluent.getPrimary().getJavaType())
            .addMember("keyProperty", "$S", fluent.getPrimary().getProperty())
            .addMember("keyColumn", "$S", fluent.getPrimary().getColumn())
            .addMember("before", "$L", before)
            .addMember("statement", "$S", seqName)
            .build());
    }

    private void addOptions(MethodSpec.Builder builder) {
        builder.addAnnotation(AnnotationSpec.builder(Options.class)
            .addMember("useGeneratedKeys", "true")
            .addMember("keyProperty", "$S", fluent.getPrimary().getProperty())
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
     * @Override
     * public abstract Xyz methodName(...);
     * </pre>
     *
     * @param methodName
     * @return
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
     * @param fluentEntity
     * @return
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
     * @return
     */
    private AnnotationSpec annotation_ResultMap() {
        return AnnotationSpec.builder(ResultMap.class).addMember("value", "ResultMap").build();
    }

    /**
     * <pre>
     *      @Results(id="", value={@Result()}
     * </pre>
     *
     * @return
     */
    private AnnotationSpec annotation_Results() {
        List<CodeBlock> results = new ArrayList<>();
        for (FieldColumn field : fluent.getFields()) {
            List<CodeBlock> blocks = new ArrayList<>();
            blocks.add(CodeBlock.of("@$T(", Result.class));
            blocks.add(CodeBlock.of("column = $S", field.getColumn()));
            blocks.add(CodeBlock.of(", property = $S", field.getProperty()));
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
     * @param value
     * @return
     */
    private AnnotationSpec annotation_Param(String value) {
        return AnnotationSpec.builder(Param.class).addMember("value", "$L", value).build();
    }
}