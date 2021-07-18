package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.segment.where.*;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * Query&Updater辅助类文件生成
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
public class WrapperHelperFiler extends AbstractFiler {
    public WrapperHelperFiler(FluentEntity fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = getClassName(fluent);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(MybatisUtil.class, "assertNotNull");
        super.staticImport(spec);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addField(this.f_defaults())
            .addType(this.nestedISegment())
            .addType(this.nestedSelector())
            .addType(this.nestedQueryWhere())
            .addType(this.nestedUpdateWhere())
            .addType(this.nestedGroupBy())
            .addType(this.nestedHaving())
            .addType(this.nestedQueryOrderBy())
            .addType(this.nestedUpdateOrderBy())
            .addType(this.nestedUpdateSetter());
    }

    private FieldSpec f_defaults() {
        return FieldSpec.builder(fluent.defaults(),
            "defaults", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("默认设置器")
            .initializer("$T.INSTANCE", fluent.defaults())
            .build();
    }

    /**
     * public interface ISegment<R> {}
     *
     * @return TypeSpec
     */
    private TypeSpec nestedISegment() {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(Suffix_ISegment)
            .addTypeVariable(TypeVariableName.get("R"))
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addMethod(this.m_set_ISegment());
        for (CommonField fc : fluent.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getName())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(TypeVariableName.get("R"))
                .addStatement("return this.set($T.$L)", fluent.mapping(), fc.getName())
                .build()
            );
        }
        return builder.build();
    }

    /**
     * public static final class GroupBy extends GroupByBase<GroupBy, EntityQuery>{}
     *
     * @return TypeSpec
     */
    private TypeSpec nestedGroupBy() {
        return TypeSpec.classBuilder(Suffix_GroupBy)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(GroupByBase.class),
                fluent.groupBy(),
                fluent.query()
            ))
            .addSuperinterface(super.parameterizedType(
                fluent.segment(),
                fluent.groupBy()
            ))
            .addJavadoc("分组设置")
            .addMethod(this.constructor1())
            .build();
    }

    /**
     * public static final class Having extends HavingBase<Having, EntityQuery>
     *
     * @return TypeSpec
     */
    private TypeSpec nestedHaving() {
        return TypeSpec.classBuilder(Suffix_Having)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(HavingBase.class),
                fluent.having(),
                fluent.query()
            ))
            .addSuperinterface(super.parameterizedType(
                fluent.segment(),
                super.parameterizedType(ClassName.get(HavingOperator.class), fluent.having())
            ))
            .addJavadoc("分组Having条件设置")
            .addMethod(this.constructor1())
            .addMethod(this.constructor2_Having())
            .addMethod(this.m_aggregate_Having())
            .build();
    }

    /**
     * public static final class QueryOrderBy extends OrderByBase<QueryOrderBy, EntityQuery>
     *
     * @return TypeSpec
     */
    private TypeSpec nestedQueryOrderBy() {
        return TypeSpec.classBuilder(Suffix_QueryOrderBy)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(OrderByBase.class),
                fluent.queryOrderBy(),
                fluent.query()
            ))
            .addSuperinterface(super.parameterizedType(
                fluent.segment(),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    fluent.queryOrderBy(),
                    fluent.query())
            ))
            .addJavadoc("Query OrderBy设置")
            .addMethod(this.constructor1())
            .build();
    }

    /**
     * public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, EntityUpdate>
     *
     * @return TypeSpec
     */
    private TypeSpec nestedUpdateOrderBy() {
        return TypeSpec.classBuilder(Suffix_UpdateOrderBy)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(OrderByBase.class),
                fluent.updateOrderBy(),
                fluent.updater()
            ))
            .addSuperinterface(super.parameterizedType(
                fluent.segment(),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    fluent.updateOrderBy(),
                    fluent.updater())
            ))
            .addJavadoc("Update OrderBy设置")
            .addMethod(this.constructor1_Update())
            .build();
    }

    /**
     * public static final class UpdateSetter extends UpdateBase<UpdateSetter, EntityUpdate>
     *
     * @return TypeSpec
     */
    private TypeSpec nestedUpdateSetter() {
        return TypeSpec.classBuilder(Suffix_UpdateSetter)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(UpdateBase.class),
                fluent.updateSetter(),
                fluent.updater()
            ))
            .addSuperinterface(super.parameterizedType(
                fluent.segment(),
                super.parameterizedType(
                    ClassName.get(UpdateApply.class),
                    fluent.updateSetter(),
                    fluent.updater())
            ))
            .addJavadoc("Update set 设置")
            .addMethod(this.constructor1_Update())
            .build();
    }

    private TypeSpec nestedSelector() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_Selector)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(SelectorBase.class),
                fluent.selector(),
                fluent.query()
            ))
            .addSuperinterface(super.parameterizedType(fluent.segment(), fluent.selector()))
            .addJavadoc("select字段设置")
            .addMethod(this.constructor1())
            .addMethod(this.constructor2_Selector())
            .addMethod(this.m_aggregate_Selector());
        for (CommonField fc : fluent.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getName())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "_alias_")
                .returns(fluent.selector())
                .addStatement("return this.process($T.$L, _alias_)", fluent.mapping(), fc.getName())
                .build()
            );
        }
        return builder.build();
    }

    /**
     * public static class QueryWhere extends ...
     *
     * @return TypeSpec
     */
    private TypeSpec nestedQueryWhere() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_QueryWhere)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .superclass(super.parameterizedType(
                ClassName.get(WhereBase.class),
                fluent.queryWhere(),
                fluent.query(),
                fluent.query()
            ))
            .addJavadoc("query where条件设置")
            .addMethod(this.construct1_QueryWhere())
            .addMethod(this.construct2_QueryWhere())
            .addMethod(this.m_buildOr_QueryWhere())
            .addMethod(this.m_queryDefaults());
        for (CommonField fc : fluent.getFields()) {
            buildWhereCondition(builder, fc, Suffix_QueryWhere);
        }
        return builder.build();
    }

    private MethodSpec m_queryDefaults() {
        return super.publicMethod("defaults", true, fluent.queryWhere())
            .addStatement("defaults.setQueryDefault(($T) super.wrapper)", fluent.query())
            .addStatement("return super.and")
            .build();
    }

    private MethodSpec m_updaterDefaults() {
        return super.publicMethod("defaults", true, fluent.updateWhere())
            .addStatement("defaults.setUpdateDefault(($T) super.wrapper)", fluent.updater())
            .addStatement("return super.and")
            .build();
    }

    /**
     * public static class QueryWhere extends ...
     *
     * @return TypeSpec
     */
    private TypeSpec nestedUpdateWhere() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_UpdateWhere)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .superclass(super.parameterizedType(
                ClassName.get(WhereBase.class),
                fluent.updateWhere(),
                fluent.updater(),
                fluent.query()
            ))
            .addJavadoc("update where条件设置")
            .addMethod(this.construct1_UpdateWhere())
            .addMethod(this.construct2_UpdateWhere())
            .addMethod(this.m_buildOr_UpdateWhere())
            .addMethod(this.m_updaterDefaults());
        for (CommonField fc : fluent.getFields()) {
            buildWhereCondition(builder, fc, Suffix_UpdateWhere);
        }
        return builder.build();
    }

    private void buildWhereCondition(TypeSpec.Builder builder, CommonField fc, String suffix_queryWhere) {
        MethodSpec.Builder field = MethodSpec
            .methodBuilder(fc.getName())
            .addModifiers(Modifier.PUBLIC);
        String klassName = fc.getJavaType().toString();
        try {
            Class klass = Class.forName(klassName);
            if (klass.equals(String.class)) {
                field.returns(whereType(suffix_queryWhere, StringWhere.class));
            } else if (klass.equals(Boolean.class)) {
                field.returns(whereType(suffix_queryWhere, BooleanWhere.class));
            } else if (Number.class.isAssignableFrom(klass)) {
                field.returns(whereType(suffix_queryWhere, NumericWhere.class));
            } else {
                field.returns(whereType(suffix_queryWhere, ObjectWhere.class));
            }
        } catch (Exception e) {
            field.returns(whereType(suffix_queryWhere, ObjectWhere.class));
        }

        field.addStatement("return this.set($T.$L)", fluent.mapping(), fc.getName());
        builder.addMethod(field.build());
    }

    private TypeName whereType(String suffix_queryWhere, Class<? extends BaseWhere> whereKlass) {
        return parameterizedType(ClassName.get(whereKlass), TypeVariableName.get(suffix_queryWhere), fluent.query());
    }

    /**
     * protected Selector aggregateSegment(IAggregate aggregate)
     *
     * @return MethodSpec
     */
    private MethodSpec m_aggregate_Having() {
        return MethodSpec.methodBuilder("aggregateSegment")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(ClassName.get(Override.class))
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .returns(fluent.having())
            .addStatement("return new Having(this, aggregate)")
            .build();
    }

    /**
     * protected Selector aggregateSegment(IAggregate aggregate)
     *
     * @return MethodSpec
     */
    private MethodSpec m_aggregate_Selector() {
        return MethodSpec.methodBuilder("aggregateSegment")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(ClassName.get(Override.class))
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .returns(fluent.selector())
            .addStatement("return new Selector(this, aggregate)")
            .build();
    }

    /**
     * public Selector(AddressQuery query)
     * public GroupBy(AddressQuery query)
     *
     * @return MethodSpec
     */
    private MethodSpec constructor1() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(fluent.query(), "query")
            .addStatement("super(query)")
            .build();
    }

    private MethodSpec constructor1_Update() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(fluent.updater(), "updater")
            .addStatement("super(updater)")
            .build();
    }

    /**
     * protected Having(Having having, IAggregate aggregate)
     *
     * @return MethodSpec
     */
    private MethodSpec constructor2_Having() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PROTECTED)
            .addParameter(fluent.having(), "having")
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .addStatement("super(having, aggregate)")
            .build();
    }

    /**
     * protected Selector(Selector selector, IAggregate aggregate)
     *
     * @return MethodSpec
     */
    private MethodSpec constructor2_Selector() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PROTECTED)
            .addParameter(fluent.selector(), "selector")
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .addStatement("super(selector, aggregate)")
            .build();
    }

    /**
     * R set(FieldMapping fieldMapping);
     *
     * @return MethodSpec
     */
    private MethodSpec m_set_ISegment() {
        return MethodSpec.methodBuilder("set")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addParameter(ClassName.get(FieldMapping.class), "fieldMapping")
            .returns(TypeVariableName.get("R"))
            .build();
    }

    /**
     * public QueryWhere(AddressQuery query)
     *
     * @return MethodSpec
     */
    private MethodSpec construct1_QueryWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(fluent.query(), "query")
            .addStatement("super(query)")
            .build();
    }

    /**
     * private QueryWhere(AddressQuery query, QueryWhere where)
     *
     * @return MethodSpec
     */
    private MethodSpec construct2_QueryWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(fluent.query(), "query")
            .addParameter(fluent.queryWhere(), "where")
            .addStatement("super(query, where)")
            .build();
    }

    /**
     * public UpdateWhere(AddressUpdate update)
     *
     * @return MethodSpec
     */
    private MethodSpec construct1_UpdateWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(fluent.updater(), "updater")
            .addStatement("super(updater)")
            .build();
    }

    /**
     * private UpdateWhere(AddressUpdate update, UpdateWhere where)
     *
     * @return MethodSpec
     */
    private MethodSpec construct2_UpdateWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(fluent.updater(), "updater")
            .addParameter(fluent.updateWhere(), "where")
            .addStatement("super(updater, where)")
            .build();
    }

    /**
     * protected QueryWhere buildOr(QueryWhere and){}
     *
     * @return MethodSpec
     */
    private MethodSpec m_buildOr_QueryWhere() {
        return MethodSpec.methodBuilder("buildOr")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Override.class)
            .addParameter(fluent.queryWhere(), "and")
            .returns(fluent.queryWhere())
            .addStatement("return new QueryWhere(($T) this.wrapper, and)", fluent.query())
            .build();
    }

    /**
     * protected UpdateWhere buildOr(UpdateWhere and) {}
     *
     * @return MethodSpec
     */
    private MethodSpec m_buildOr_UpdateWhere() {
        return MethodSpec.methodBuilder("buildOr")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Override.class)
            .addParameter(fluent.updateWhere(), "and")
            .returns(fluent.updateWhere())
            .addStatement("return new UpdateWhere(($T) this.wrapper, and)", fluent.updater())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_WrapperHelper;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Helper);
    }
}