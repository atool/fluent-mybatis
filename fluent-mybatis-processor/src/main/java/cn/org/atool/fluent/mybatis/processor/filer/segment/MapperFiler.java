package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.Mybatis_UnknownTypeHandler;

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
        spec.addStaticImport(ClassName.get(FluentConst.class), "*");
        spec.addStaticImport(fluent.entityMapping(), Suffix_MAPPING);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addSuperinterface(paraType(ClassName.get(IWrapperMapper.class), fluent.entity(), fluent.query(), fluent.updater()))
            .addSuperinterface(paraType(ClassNames2.getClassName(fluent.getSuperMapper()), fluent.entity()))
            .addAnnotation(ClassNames2.Mybatis_Mapper)
            .addAnnotation(AnnotationSpec.builder(ClassNames2.Spring_Component)
                .addMember("value", "$S", getMapperName(this.fluent)).build()
            );
        if (fluent.isUsedCached()) {
            spec.addAnnotation(AnnotationSpec.builder(CacheNamespace.class)
                .addMember("blocking", "true").build()
            );
        }

        spec.addMethod(this.m_insert())
            .addMethod(this.m_insertBatch())
            .addMethod(this.m_listEntity());

        spec.addMethod(this.m_mapping());
    }

    @Override
    protected MethodSpec m_mapping() {
        return this.publicMethod(Suffix_mapping, IMapping.class)
            .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
            .addStatement("return $L", Suffix_MAPPING)
            .build();
    }

    public MethodSpec m_listEntity() {
        return this.mapperMethod(SelectProvider.class, M_listEntity)
            .addAnnotation(this.annotation_Results())
            .addParameter(queryParam())
            .returns(paraType(ClassName.get(List.class), fluent.entity()))
            .build();
    }

    public MethodSpec m_insertBatch() {
        MethodSpec.Builder builder = this.mapperMethod(InsertProvider.class, M_InsertBatch);
        if (fluent.getPrimary() != null) {
            if (fluent.getPrimary().isAutoIncrease() && isBlank(fluent.getPrimary().getSeqName())) {
                this.addOptions(builder);
            } else {
                switch (fluent.getDbType()) {
                    case ORACLE:
                    case ORACLE12:
                        this.addOptions(builder);
                        break;
                    default:
                        this.addSelectKey(builder);
                }
            }
        }
        TypeName listType = paraType(CN_Collection, fluent.entity());
        return builder.addParameter(this.param(listType))
            .returns(TypeName.INT)
            .build();
    }

    private ParameterSpec param(TypeName type) {
        return ParameterSpec.builder(type, "entities")
            .addAnnotation(annotation_Param("Param_List"))
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
        String seqName = fluent.getDbType().feature.getSeq();
        boolean before = fluent.getDbType().feature.isBefore();
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
        if (fluent.getPrimary() == null) {
            return;
        }
        AnnotationSpec.Builder ab = AnnotationSpec.builder(Options.class);
        switch (fluent.getDbType()) {
            case ORACLE:
            case ORACLE12:
                ab.addMember("useGeneratedKeys", "$L", false);
                break;
            default:
                ab.addMember("useGeneratedKeys", "$L", true);
        }
        ab.addMember("keyProperty", "$S", fluent.getPrimary().getName());
        ab.addMember("keyColumn", "$S", fluent.getPrimary().getColumn());
        builder.addAnnotation(ab.build());
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
            .addMember("type", "$T.class", SqlProvider.class)
            .addMember("method", "$S", methodName)
            .build());
        return builder;
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
     *      Results(id="", value={Result()}
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
            if (field.getJdbcType() != null && !Objects.equals(JdbcType.UNDEFINED.name(), field.getJdbcType())) {
                blocks.add(CodeBlock.of(", jdbcType = $T.$L", JdbcType.class, field.getJdbcType()));
            }
            if (field.getTypeHandler() != null && !Objects.equals(Mybatis_UnknownTypeHandler, field.getTypeHandler())) {
                blocks.add(CodeBlock.of(", typeHandler = $T.class", field.getTypeHandler()));
            }
            blocks.add(CodeBlock.of(")"));
            results.add(CodeBlock.join(blocks, ""));
        }
        return AnnotationSpec.builder(Results.class)
            .addMember("value", "{\n$L\n}", CodeBlock.join(results, ",\n"))
            .build();
    }

    /**
     * <pre>
     *      Param("value") 注解
     * </pre>
     *
     * @param value 注解属性值
     * @return AnnotationSpec
     */
    private AnnotationSpec annotation_Param(String value) {
        return AnnotationSpec.builder(Param.class).addMember("value", "$L", value).build();
    }

    /**
     * 声明  @Param("paraName") varName
     *
     * @return @Param("paraName") varName
     */
    private ParameterSpec queryParam() {
        return ParameterSpec.builder(IQuery.class, "query")
            .addAnnotation(annotation_Param("Param_EW"))
            .build();
    }
}