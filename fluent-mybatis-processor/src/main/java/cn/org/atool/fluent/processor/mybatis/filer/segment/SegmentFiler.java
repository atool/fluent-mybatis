package cn.org.atool.fluent.processor.mybatis.filer.segment;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.segment.where.BooleanWhere;
import cn.org.atool.fluent.mybatis.segment.where.NumericWhere;
import cn.org.atool.fluent.mybatis.segment.where.ObjectWhere;
import cn.org.atool.fluent.mybatis.segment.where.StringWhere;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.processor.mybatis.base.FluentClassName;
import cn.org.atool.fluent.processor.mybatis.entity.CommonField;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.processor.mybatis.filer.AbstractFiler;
import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;

import static cn.org.atool.fluent.common.kits.StringKit.PRE_SET;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.processor.mybatis.filer.FilerKit.PUBLIC_STATIC_FINAL;
import static cn.org.atool.fluent.processor.mybatis.filer.FilerKit.suppressWarnings;

/**
 * Query and Updater辅助类文件生成
 *
 * @author wudarui
 */
@SuppressWarnings({ "rawtypes" })
public class SegmentFiler extends AbstractFiler {
    /**
     * 构造函数
     *
     * @param fluent FluentEntity
     */
    public SegmentFiler(FluentEntity fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = getClassName(fluent);
    }

    /**
     * 获取类名
     *
     * @param fluent FluentClassName
     * @return class name
     */
    public static String getClassName(FluentClassName fluent) {
        return fluent.getNoSuffix() + Suffix_Segment;
    }

    /**
     * 获取包名
     *
     * @param fluent FluentClassName
     * @return package name
     */
    public static String getPackageName(FluentClassName fluent) {
        return fluent.getPackageName(Pack_Helper);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(MybatisUtil.class, "assertNotNull");
        spec.addStaticImport(fluent.entityMapping(), "*");
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addAnnotation(suppressWarnings("unused", "rawtypes", "unchecked"));
        spec.addType(this.nestedISegment())
                .addType(this.nestedSelector())
                .addType(this.nestedEntityWhere())
                .addType(this.nestedQueryWhere())
                .addType(this.nestedUpdateWhere())
                .addType(this.nestedGroupBy())
                .addType(this.nestedHaving())
                .addType(this.nestedQueryOrderBy())
                .addType(this.nestedUpdateOrderBy())
                .addType(this.nestedUpdateSetter());
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
                    .addStatement("return this.set($L)", fc.getName())
                    .build());
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
                .addModifiers(PUBLIC_STATIC_FINAL)
                .superclass(super.paraType(
                        ClassName.get(GroupByBase.class),
                        fluent.groupBy(),
                        fluent.query()))
                .addSuperinterface(super.paraType(
                        fluent.segment(),
                        fluent.groupBy()))
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
                .addModifiers(PUBLIC_STATIC_FINAL)
                .superclass(super.paraType(
                        ClassName.get(HavingBase.class),
                        fluent.having(),
                        fluent.query()))
                .addSuperinterface(super.paraType(
                        fluent.segment(),
                        super.paraType(ClassName.get(HavingOperator.class), fluent.having())))
                .addJavadoc("分组Having条件设置")
                .addMethod(this.constructor1())
                .addMethod(this.constructor2_Having())
                .addMethod(this.m_aggregate_Having())
                .build();
    }

    /**
     * public static final class QueryOrderBy extends OrderByBase<QueryOrderBy,
     * EntityQuery>
     *
     * @return TypeSpec
     */
    private TypeSpec nestedQueryOrderBy() {
        return TypeSpec.classBuilder(Suffix_QueryOrderBy)
                .addModifiers(PUBLIC_STATIC_FINAL)
                .superclass(super.paraType(
                        ClassName.get(OrderByBase.class),
                        fluent.queryOrderBy(),
                        fluent.query()))
                .addSuperinterface(super.paraType(
                        fluent.segment(),
                        super.paraType(
                                ClassName.get(OrderByApply.class),
                                fluent.queryOrderBy(),
                                fluent.query())))
                .addJavadoc("Query OrderBy设置")
                .addMethod(this.constructor1())
                .build();
    }

    /**
     * public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy,
     * EntityUpdate>
     *
     * @return TypeSpec
     */
    private TypeSpec nestedUpdateOrderBy() {
        return TypeSpec.classBuilder(Suffix_UpdateOrderBy)
                .addModifiers(PUBLIC_STATIC_FINAL)
                .superclass(super.paraType(
                        ClassName.get(OrderByBase.class),
                        fluent.updateOrderBy(),
                        fluent.updater()))
                .addSuperinterface(super.paraType(
                        fluent.segment(),
                        super.paraType(
                                ClassName.get(OrderByApply.class),
                                fluent.updateOrderBy(),
                                fluent.updater())))
                .addJavadoc("Update OrderBy设置")
                .addMethod(this.constructor1_Update())
                .build();
    }

    /**
     * public static final class UpdateSetter extends UpdateBase<UpdateSetter,
     * EntityUpdate>
     *
     * @return TypeSpec
     */
    private TypeSpec nestedUpdateSetter() {
        return TypeSpec.classBuilder(Suffix_UpdateSetter)
                .addModifiers(PUBLIC_STATIC_FINAL)
                .superclass(super.paraType(
                        ClassName.get(UpdateBase.class),
                        fluent.updateSetter(),
                        fluent.updater()))
                .addSuperinterface(super.paraType(
                        fluent.segment(),
                        super.paraType(
                                ClassName.get(UpdateApply.class),
                                fluent.updateSetter(),
                                fluent.updater())))
                .addJavadoc("Update set 设置")
                .addMethod(this.constructor1_Update())
                .build();
    }

    private TypeSpec nestedSelector() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_Selector)
                .addModifiers(PUBLIC_STATIC_FINAL)
                .superclass(super.paraType(
                        ClassName.get(SelectorBase.class),
                        fluent.selector(),
                        fluent.query()))
                .addSuperinterface(super.paraType(fluent.segment(), fluent.selector()))
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
                    .addStatement("return this.process($L, _alias_)", varAggregate(fc.getName()))
                    .build());
        }
        return builder.build();
    }

    private TypeSpec nestedEntityWhere() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_EntityWhere)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.ABSTRACT)
                .addTypeVariable(TypeVariableName.get("W", paraType(WhereBase.class, "W", "U", fluent.query())))
                .addTypeVariable(TypeVariableName.get("U", paraType(IWrapper.class, "?", "U", fluent.query())))
                .superclass(paraType(WhereBase.class, "W", "U", fluent.query()))
                .addJavadoc("query/update where条件设置")
                .addMethod(this.construct1_EntityWhere())
                .addMethod(this.construct2_EntityWhere());
        for (CommonField fc : fluent.getFields()) {
            buildWhereCondition(builder, fc);
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
                .superclass(super.paraType("EntityWhere", Suffix_QueryWhere, fluent.query()))
                .addJavadoc("query where条件设置")
                .addMethod(this.construct1_QueryWhere())
                .addMethod(this.construct2_QueryWhere())
                .addMethod(this.m_buildOr_QueryWhere());
        return builder.build();
    }

    /**
     * public static class QueryWhere extends ...
     *
     * @return TypeSpec
     */
    private TypeSpec nestedUpdateWhere() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_UpdateWhere)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .superclass(super.paraType("EntityWhere", Suffix_UpdateWhere, fluent.updater()))
                .addJavadoc("update where条件设置")
                .addMethod(this.construct1_UpdateWhere())
                .addMethod(this.construct2_UpdateWhere())
                .addMethod(this.m_buildOr_UpdateWhere());
        return builder.build();
    }

    private void buildWhereCondition(TypeSpec.Builder builder, CommonField fc) {
        MethodSpec.Builder field = MethodSpec
                .methodBuilder(fc.getName())
                .addModifiers(Modifier.PUBLIC);
        String klassName = fc.getJavaType().toString();
        try {
            Class klass = Class.forName(klassName);
            if (klass.equals(String.class)) {
                field.returns(this.whereType(StringWhere.class));
            } else if (klass.equals(Boolean.class)) {
                field.returns(this.whereType(BooleanWhere.class));
            } else if (Number.class.isAssignableFrom(klass)) {
                field.returns(this.whereType(NumericWhere.class));
            } else {
                field.returns(this.whereType(ObjectWhere.class));
            }
        } catch (Exception e) {
            field.returns(this.whereType(ObjectWhere.class));
        }

        field.addStatement("return this.set($L)", varSegment(fc.getName()));
        builder.addMethod(field.build());
    }

    private TypeName whereType(Class whereKlass) {
        return paraType(ClassName.get(whereKlass), TypeVariableName.get("W"), fluent.query());
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
        return MethodSpec.methodBuilder(PRE_SET)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(ClassName.get(FieldMapping.class), "fieldMapping")
                .returns(TypeVariableName.get("R"))
                .build();
    }

    private MethodSpec construct1_EntityWhere() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeVariableName.get("U"), "wrapper")
                .addStatement("super(wrapper)")
                .build();
    }

    private MethodSpec construct2_EntityWhere() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(TypeVariableName.get("U"), "wrapper")
                .addParameter(TypeVariableName.get("W"), "where")
                .addStatement("super(wrapper, where)")
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
        return true;
    }

    /**
     * 在 {@link AggregateSegment} 中定义过的变量
     */
    static List<String> CONFLICT_AGGREGATE = Arrays.asList(
            "origin", "aggregate", "max", "min", "sum", "avg", "count", "group_concat");
    /**
     * 在 {@link BaseSegment}, {@link WhereBase} 中定义过的变量
     */
    static List<String> CONFLICT_SEGMENT = Arrays.asList(
            "and", "or", "wrapper", "current", "apply");

    private String varAggregate(String name) {
        if (CONFLICT_AGGREGATE.contains(name) || CONFLICT_SEGMENT.contains(name)) {
            return fluent.entityMapping().simpleName() + "." + name;
        } else {
            return name;
        }
    }

    private String varSegment(String name) {
        if (CONFLICT_SEGMENT.contains(name)) {
            return fluent.entityMapping().simpleName() + "." + name;
        } else {
            return name;
        }
    }
}