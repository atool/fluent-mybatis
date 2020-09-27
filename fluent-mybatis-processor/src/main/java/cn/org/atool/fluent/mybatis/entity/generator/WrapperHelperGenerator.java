package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.entity.EntityKlass;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class WrapperHelperGenerator extends AbstractGenerator {
    public WrapperHelperGenerator(TypeElement curElement, EntityKlass entityKlass) {
        super(curElement, entityKlass);
        this.packageName = getPackageName(entityKlass);
        this.klassName = getClassName(entityKlass);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addSuperinterface(MappingGenerator.className(entityKlass))
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
        for (FieldColumn fc : entityKlass.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getProperty())
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .returns(TypeVariableName.get("R"))
                .addStatement("return this.set($T.$L)", MappingGenerator.className(entityKlass), fc.getProperty())
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
                groupBy(entityKlass),
                QueryGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(entityKlass),
                groupBy(entityKlass)
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
                having(entityKlass),
                QueryGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(entityKlass),
                super.parameterizedType(ClassName.get(HavingOperator.class), having(entityKlass))
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
                queryOrderBy(entityKlass),
                QueryGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(entityKlass),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    queryOrderBy(entityKlass),
                    QueryGenerator.className(entityKlass))
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
                updateOrderBy(entityKlass),
                UpdaterGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(entityKlass),
                super.parameterizedType(
                    ClassName.get(OrderByApply.class),
                    updateOrderBy(entityKlass),
                    UpdaterGenerator.className(entityKlass))
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
                updateSetter(entityKlass),
                UpdaterGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(entityKlass),
                super.parameterizedType(
                    ClassName.get(UpdateApply.class),
                    updateSetter(entityKlass),
                    UpdaterGenerator.className(entityKlass))
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
                selector(entityKlass),
                QueryGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(segment(entityKlass), selector(entityKlass)))
            .addJavadoc("select字段设置")
            .addMethod(this.constructor1())
            .addMethod(this.constructor2_Selector())
            .addMethod(this.m_aggregate_Selector());
        for (FieldColumn fc : entityKlass.getFields()) {
            builder.addMethod(MethodSpec
                .methodBuilder(fc.getProperty())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "alias")
                .returns(selector(entityKlass))
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
                queryWhere(this.entityKlass),
                QueryGenerator.className(entityKlass),
                QueryGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(entityKlass),
                super.parameterizedType(
                    ClassName.get(WhereApply.class),
                    queryWhere(this.entityKlass),
                    QueryGenerator.className(entityKlass)
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
                updateWhere(this.entityKlass),
                UpdaterGenerator.className(entityKlass),
                QueryGenerator.className(entityKlass)
            ))
            .addSuperinterface(super.parameterizedType(
                segment(entityKlass),
                super.parameterizedType(
                    ClassName.get(WhereApply.class),
                    updateWhere(this.entityKlass),
                    QueryGenerator.className(entityKlass)
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
            .returns(having(entityKlass))
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
            .returns(selector(entityKlass))
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
            .addParameter(QueryGenerator.className(entityKlass), "query")
            .addStatement("super(query)")
            .build();
    }

    private MethodSpec constructor1_Update() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UpdaterGenerator.className(entityKlass), "updater")
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
            .addParameter(having(entityKlass), "having")
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
            .addParameter(selector(entityKlass), "selector")
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
            .addParameter(QueryGenerator.className(entityKlass), "query")
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
            .addParameter(QueryGenerator.className(entityKlass), "query")
            .addParameter(queryWhere(this.entityKlass), "where")
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
            .addParameter(UpdaterGenerator.className(entityKlass), "updater")
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
            .addParameter(UpdaterGenerator.className(entityKlass), "updater")
            .addParameter(updateWhere(this.entityKlass), "where")
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
            .addParameter(queryWhere(this.entityKlass), "and")
            .returns(queryWhere(this.entityKlass))
            .addStatement("return new QueryWhere(($T) this.wrapper, and)", QueryGenerator.className(entityKlass))
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
            .addParameter(updateWhere(this.entityKlass), "and")
            .returns(updateWhere(this.entityKlass))
            .addStatement("return new UpdateWhere(($T) this.wrapper, and)", UpdaterGenerator.className(entityKlass))
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    public static ClassName queryWhere(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "QueryWhere");
    }

    public static ClassName updateWhere(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "UpdateWhere");
    }

    public static ClassName selector(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "Selector");
    }

    public static ClassName groupBy(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "GroupBy");
    }

    public static ClassName having(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "Having");
    }

    public static ClassName queryOrderBy(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "QueryOrderBy");
    }

    public static ClassName updateOrderBy(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "UpdateOrderBy");
    }

    public static ClassName updateSetter(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "UpdateSetter");
    }

    public static ClassName segment(EntityKlass entityKlass) {
        return ClassName.get(
            getPackageName(entityKlass) + "." + getClassName(entityKlass),
            "ISegment");
    }

    public static String getClassName(EntityKlass entityKlass) {
        return entityKlass.getNoSuffix() + "WrapperHelper";
    }

    public static String getPackageName(EntityKlass entityKlass) {
        return entityKlass.getPackageName("helper");
    }
}