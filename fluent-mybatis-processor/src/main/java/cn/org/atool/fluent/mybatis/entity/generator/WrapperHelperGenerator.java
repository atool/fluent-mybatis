package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class WrapperHelperGenerator extends AbstractGenerator {
    public WrapperHelperGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addSuperinterface(MappingGenerator.className(fluentEntityInfo))
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
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder("ISegment")
            .addTypeVariable(TypeVariableName.get("R"))
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addMethod(this.m_set_ISegment());
        for (FieldColumn fc : fluentEntityInfo.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getProperty())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(TypeVariableName.get("R"))
                .addStatement("return this.set($T.$L)", MappingGenerator.className(fluentEntityInfo), fc.getProperty())
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
        return TypeSpec.classBuilder("GroupBy")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(GroupByBase.class),
                groupBy(fluentEntityInfo),
                QueryGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluentEntityInfo),
                groupBy(fluentEntityInfo)
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
        return TypeSpec.classBuilder("Having")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(HavingBase.class),
                having(fluentEntityInfo),
                QueryGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluentEntityInfo),
                super.parameterizedType(ClassName.get(HavingOperator.class), having(fluentEntityInfo))
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
        return TypeSpec.classBuilder("QueryOrderBy")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(OrderByBase.class),
                queryOrderBy(fluentEntityInfo),
                QueryGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluentEntityInfo),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    queryOrderBy(fluentEntityInfo),
                    QueryGenerator.className(fluentEntityInfo))
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
        return TypeSpec.classBuilder("UpdateOrderBy")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(OrderByBase.class),
                updateOrderBy(fluentEntityInfo),
                UpdaterGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluentEntityInfo),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    updateOrderBy(fluentEntityInfo),
                    UpdaterGenerator.className(fluentEntityInfo))
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
        return TypeSpec.classBuilder("UpdateSetter")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(UpdateBase.class),
                updateSetter(fluentEntityInfo),
                UpdaterGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluentEntityInfo),
                super.parameterizedType(
                    ClassName.get(UpdateApply.class),
                    updateSetter(fluentEntityInfo),
                    UpdaterGenerator.className(fluentEntityInfo))
            ))
            .addJavadoc("Update set 设置")
            .addMethod(this.constructor1_Update())
            .build();
    }

    private TypeSpec nestedSelector() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("Selector")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(
                ClassName.get(SelectorBase.class),
                selector(fluentEntityInfo),
                QueryGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(segment(fluentEntityInfo), selector(fluentEntityInfo)))
            .addJavadoc("select字段设置")
            .addMethod(this.constructor1())
            .addMethod(this.constructor2_Selector())
            .addMethod(this.m_aggregate_Selector());
        for (FieldColumn fc : fluentEntityInfo.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getProperty())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "alias")
                .returns(selector(fluentEntityInfo))
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
        return TypeSpec.classBuilder("QueryWhere")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .superclass(super.parameterizedType(
                ClassName.get(WhereBase.class),
                queryWhere(this.fluentEntityInfo),
                QueryGenerator.className(fluentEntityInfo),
                QueryGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluentEntityInfo),
                super.parameterizedType(
                    ClassName.get(WhereApply.class),
                    queryWhere(this.fluentEntityInfo),
                    QueryGenerator.className(fluentEntityInfo)
                )
            ))
            .addJavadoc("query where条件设置")
            .addMethod(this.construct1_QueryWhere())
            .addMethod(this.construct2_QueryWhere())
            .addMethod(this.m_buildOr_QueryWhere())
            .build();
    }

    /**
     * public static class QueryWhere extends ...
     *
     * @return
     */
    private TypeSpec nestedUpdateWhere() {
        return TypeSpec.classBuilder("UpdateWhere")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .superclass(super.parameterizedType(
                ClassName.get(WhereBase.class),
                updateWhere(this.fluentEntityInfo),
                UpdaterGenerator.className(fluentEntityInfo),
                QueryGenerator.className(fluentEntityInfo)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(fluentEntityInfo),
                super.parameterizedType(
                    ClassName.get(WhereApply.class),
                    updateWhere(this.fluentEntityInfo),
                    QueryGenerator.className(fluentEntityInfo)
                )
            ))
            .addJavadoc("update where条件设置")
            .addMethod(this.construct1_UpdateWhere())
            .addMethod(this.construct2_UpdateWhere())
            .addMethod(this.m_buildOr_UpdateWhere())
            .build();
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
            .returns(having(fluentEntityInfo))
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
            .returns(selector(fluentEntityInfo))
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
            .addParameter(QueryGenerator.className(fluentEntityInfo), "query")
            .addStatement("super(query)")
            .build();
    }

    private MethodSpec constructor1_Update() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UpdaterGenerator.className(fluentEntityInfo), "updater")
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
            .addParameter(having(fluentEntityInfo), "having")
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
            .addParameter(selector(fluentEntityInfo), "selector")
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
            .returns(ClassName.get("", "R"))
            .build();
    }

    /**
     * public QueryWhere(AddressQuery query)
     *
     * @return
     */
    private MethodSpec construct1_QueryWhere() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(QueryGenerator.className(fluentEntityInfo), "query")
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
            .addParameter(QueryGenerator.className(fluentEntityInfo), "query")
            .addParameter(queryWhere(this.fluentEntityInfo), "where")
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
            .addParameter(UpdaterGenerator.className(fluentEntityInfo), "updater")
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
            .addParameter(UpdaterGenerator.className(fluentEntityInfo), "updater")
            .addParameter(updateWhere(this.fluentEntityInfo), "where")
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
            .addParameter(queryWhere(this.fluentEntityInfo), "and")
            .returns(queryWhere(this.fluentEntityInfo))
            .addStatement("return new QueryWhere(($T) this.wrapper, and)", QueryGenerator.className(fluentEntityInfo))
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
            .addParameter(updateWhere(this.fluentEntityInfo), "and")
            .returns(updateWhere(this.fluentEntityInfo))
            .addStatement("return new UpdateWhere(($T) this.wrapper, and)", UpdaterGenerator.className(fluentEntityInfo))
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    public static ClassName queryWhere(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "QueryWhere");
    }

    public static ClassName updateWhere(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "UpdateWhere");
    }

    public static ClassName selector(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "Selector");
    }

    public static ClassName groupBy(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "GroupBy");
    }

    public static ClassName having(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "Having");
    }

    public static ClassName queryOrderBy(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "QueryOrderBy");
    }

    public static ClassName updateOrderBy(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "UpdateOrderBy");
    }

    public static ClassName updateSetter(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "UpdateSetter");
    }

    public static ClassName segment(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(
            getPackageName(fluentEntityInfo) + "." + getClassName(fluentEntityInfo),
            "ISegment");
    }

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + "WrapperHelper";
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName("helper");
    }
}