package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.segment.where.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.*;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

public class WrapperHelperGenerator extends AbstractGenerator {
    public WrapperHelperGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addSuperinterface(mapping(fluent))
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

    /**
     * public interface ISegment<R> {}
     *
     * @return
     */
    private TypeSpec nestedISegment() {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(Suffix_ISegment)
            .addTypeVariable(TypeVariableName.get("R"))
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addMethod(this.m_set_ISegment());
        for (FieldColumn fc : fluent.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getProperty())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(TypeVariableName.get("R"))
                .addStatement("return this.set($T.$L)", mapping(fluent), fc.getProperty())
                .build()
            );
        }
        return builder.build();
    }

    /**
     * public static final class GroupBy extends GroupByBase<GroupBy, EntityQuery>{}
     *
     * @return
     */
    private TypeSpec nestedGroupBy() {
        return TypeSpec.classBuilder(Suffix_GroupBy)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(GroupByBase.class),
                groupBy(fluent),
                query(fluent)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluent),
                groupBy(fluent)
            ))
            .addJavadoc("分组设置")
            .addMethod(this.constructor1())
            .build();
    }

    /**
     * public static final class Having extends HavingBase<Having, EntityQuery>
     *
     * @return
     */
    private TypeSpec nestedHaving() {
        return TypeSpec.classBuilder(Suffix_Having)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(HavingBase.class),
                having(fluent),
                query(fluent)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluent),
                super.parameterizedType(ClassName.get(HavingOperator.class), having(fluent))
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
     * @return
     */
    private TypeSpec nestedQueryOrderBy() {
        return TypeSpec.classBuilder(Suffix_QueryOrderBy)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(OrderByBase.class),
                queryOrderBy(fluent),
                query(fluent)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluent),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    queryOrderBy(fluent),
                    query(fluent))
            ))
            .addJavadoc("Query OrderBy设置")
            .addMethod(this.constructor1())
            .build();
    }

    /**
     * public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, EntityUpdate>
     *
     * @return
     */
    private TypeSpec nestedUpdateOrderBy() {
        return TypeSpec.classBuilder(Suffix_UpdateOrderBy)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(OrderByBase.class),
                updateOrderBy(fluent),
                updater(fluent)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluent),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    updateOrderBy(fluent),
                    updater(fluent))
            ))
            .addJavadoc("Update OrderBy设置")
            .addMethod(this.constructor1_Update())
            .build();
    }

    /**
     * public static final class UpdateSetter extends UpdateBase<UpdateSetter, EntityUpdate>
     *
     * @return
     */
    private TypeSpec nestedUpdateSetter() {
        return TypeSpec.classBuilder(Suffix_UpdateSetter)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(UpdateBase.class),
                updateSetter(fluent),
                updater(fluent)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluent),
                super.parameterizedType(
                    ClassName.get(UpdateApply.class),
                    updateSetter(fluent),
                    updater(fluent))
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
                selector(fluent),
                query(fluent)
            ))
            .addSuperinterface(super.parameterizedType(segment(fluent), selector(fluent)))
            .addJavadoc("select字段设置")
            .addMethod(this.constructor1())
            .addMethod(this.constructor2_Selector())
            .addMethod(this.m_aggregate_Selector());
        for (FieldColumn fc : fluent.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getProperty())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "alias")
                .returns(selector(fluent))
                .addStatement("return this.process($L, alias)", fc.getProperty())
                .build()
            );
        }
        return builder.build();
    }

    /**
     * public static class QueryWhere extends ...
     *
     * @return
     */
    private TypeSpec nestedQueryWhere() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_QueryWhere)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .superclass(super.parameterizedType(
                ClassName.get(WhereBase.class),
                queryWhere(this.fluent),
                query(fluent),
                query(fluent)
            ))
            .addJavadoc("query where条件设置")
            .addMethod(this.construct1_QueryWhere())
            .addMethod(this.construct2_QueryWhere())
            .addMethod(this.m_buildOr_QueryWhere());
        for (FieldColumn fc : fluent.getFields()) {
            buildWhereCondition(builder, fc, Suffix_QueryWhere);
        }
        return builder.build();
    }

    /**
     * public static class QueryWhere extends ...
     *
     * @return
     */
    private TypeSpec nestedUpdateWhere() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(Suffix_UpdateWhere)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .superclass(super.parameterizedType(
                ClassName.get(WhereBase.class),
                updateWhere(this.fluent),
                updater(fluent),
                query(fluent)
            ))
            .addJavadoc("update where条件设置")
            .addMethod(this.construct1_UpdateWhere())
            .addMethod(this.construct2_UpdateWhere())
            .addMethod(this.m_buildOr_UpdateWhere());
        for (FieldColumn fc : fluent.getFields()) {
            buildWhereCondition(builder, fc, Suffix_UpdateWhere);
        }
        return builder.build();
    }

    private void buildWhereCondition(TypeSpec.Builder builder, FieldColumn fc, String suffix_queryWhere) {
        MethodSpec.Builder field = MethodSpec
            .methodBuilder(fc.getProperty())
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

        field.addStatement("return this.set($T.$L)", mapping(fluent), fc.getProperty());
        builder.addMethod(field.build());
    }

    private TypeName whereType(String suffix_queryWhere, Class<? extends BaseWhere> whereKlass) {
        return parameterizedType(ClassName.get(whereKlass), TypeVariableName.get(suffix_queryWhere), query(fluent));
    }

    /**
     * protected Selector aggregateSegment(IAggregate aggregate)
     *
     * @return
     */
    private MethodSpec m_aggregate_Having() {
        return MethodSpec.methodBuilder("aggregateSegment")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(ClassName.get(Override.class))
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .returns(having(fluent))
            .addStatement("return new Having(this, aggregate)")
            .build();
    }

    /**
     * protected Selector aggregateSegment(IAggregate aggregate)
     *
     * @return
     */
    private MethodSpec m_aggregate_Selector() {
        return MethodSpec.methodBuilder("aggregateSegment")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(ClassName.get(Override.class))
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .returns(selector(fluent))
            .addStatement("return new Selector(this, aggregate)")
            .build();
    }

    /**
     * public Selector(AddressQuery query)
     * public GroupBy(AddressQuery query)
     *
     * @return
     */
    private MethodSpec constructor1() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(query(fluent), "query")
            .addStatement("super(query)")
            .build();
    }

    private MethodSpec constructor1_Update() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(updater(fluent), "updater")
            .addStatement("super(updater)")
            .build();
    }

    /**
     * protected Having(Having having, IAggregate aggregate)
     *
     * @return
     */
    private MethodSpec constructor2_Having() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PROTECTED)
            .addParameter(having(fluent), "having")
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .addStatement("super(having, aggregate)")
            .build();
    }

    /**
     * protected Selector(Selector selector, IAggregate aggregate)
     *
     * @return
     */
    private MethodSpec constructor2_Selector() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PROTECTED)
            .addParameter(selector(fluent), "selector")
            .addParameter(ClassName.get(IAggregate.class), "aggregate")
            .addStatement("super(selector, aggregate)")
            .build();
    }

    /**
     * R set(FieldMapping fieldMapping);
     *
     * @return
     */
    private MethodSpec m_set_ISegment() {
        return MethodSpec.methodBuilder("set")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addParameter(ClassName.get(FieldMapping.class), "fieldMapping")
            .returns(TypeVariableName.get("R"))
            .build();
    }

    /**
     * R set(FieldMapping fieldMapping);
     *
     * @return
     */

    /**
     * public QueryWhere(AddressQuery query)
     *
     * @return
     */
    private MethodSpec construct1_QueryWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(query(fluent), "query")
            .addStatement("super(query)")
            .build();
    }

    /**
     * private QueryWhere(AddressQuery query, QueryWhere where)
     *
     * @return
     */
    private MethodSpec construct2_QueryWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(query(fluent), "query")
            .addParameter(queryWhere(this.fluent), "where")
            .addStatement("super(query, where)")
            .build();
    }

    /**
     * public UpdateWhere(AddressUpdate update)
     *
     * @return
     */
    private MethodSpec construct1_UpdateWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(updater(fluent), "updater")
            .addStatement("super(updater)")
            .build();
    }

    /**
     * private UpdateWhere(AddressUpdate update, UpdateWhere where)
     *
     * @return
     */
    private MethodSpec construct2_UpdateWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(updater(fluent), "updater")
            .addParameter(updateWhere(this.fluent), "where")
            .addStatement("super(updater, where)")
            .build();
    }

    /**
     * protected QueryWhere buildOr(QueryWhere and){}
     *
     * @return
     */
    private MethodSpec m_buildOr_QueryWhere() {
        return MethodSpec.methodBuilder("buildOr")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Override.class)
            .addParameter(queryWhere(this.fluent), "and")
            .returns(queryWhere(this.fluent))
            .addStatement("return new QueryWhere(($T) this.wrapper, and)", query(fluent))
            .build();
    }

    /**
     * protected UpdateWhere buildOr(UpdateWhere and) {}
     *
     * @return
     */
    private MethodSpec m_buildOr_UpdateWhere() {
        return MethodSpec.methodBuilder("buildOr")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Override.class)
            .addParameter(updateWhere(this.fluent), "and")
            .returns(updateWhere(this.fluent))
            .addStatement("return new UpdateWhere(($T) this.wrapper, and)", updater(fluent))
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + Suffix_WrapperHelper;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Helper);
    }
}