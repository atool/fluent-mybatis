package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.mapper.StrConstant;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Wrapper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Query;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Supplier_Str;

/**
 * QueryGenerator: *Query文件生成
 *
 * @author wudarui
 */
public class QueryFiler extends AbstractFiler {

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Query;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Wrapper);
    }

    public QueryFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "查询构造";
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(If.class, "notBlank");
        spec.addStaticImport(StrConstant.class, "EMPTY");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(this.superClass())
            .addField(this.f_mapping())
            .addField(this.f_select())
            .addField(this.f_groupBy())
            .addField(this.f_having())
            .addField(this.f_orderBy())
            .addField(this.f_where());
        builder
            .addMethod(this.constructor0())
            .addMethod(this.constructor1_String())
            .addMethod(this.constructor2_String_String())
            .addMethod(this.constructor2_String_Parameter())
            .addMethod(this.m_where())
            .addMethod(this.m_mapping())
            .addMethod(this.m_emptyQuery())
            .addMethod(this.m_emptyQuery_Alias())
            .addMethod(this.m_emptyQuery_table())
            .addMethod(this.m_emptyQuery_table_Alias())
            .addMethod(this.m_defaultQuery())
            .addMethod(this.m_aliasQuery_0())
            .addMethod(this.m_aliasQuery_1_String())
            .addMethod(this.m_aliasWith_1_BaseQuery())
            .addMethod(this.m_aliasWith_2_String_BaseQuery());
    }

    private MethodSpec m_emptyQuery() {
        return super.publicMethod(M_EMPTY_QUERY, false, fluent.query())
            .addModifiers(Modifier.STATIC)
            .addStatement("return new $T()", fluent.query())
            .build();
    }

    private MethodSpec m_emptyQuery_Alias() {
        return super.publicMethod(M_EMPTY_QUERY, false, fluent.query())
            .addModifiers(Modifier.STATIC)
            .addParameter(String.class, "alias")
            .addStatement("return new $T(alias)", fluent.query())
            .build();
    }

    private MethodSpec m_emptyQuery_table() {
        return super.publicMethod(M_EMPTY_QUERY, false, fluent.query())
            .addModifiers(Modifier.STATIC)
            .addParameter(CN_Supplier_Str, "table")
            .addStatement("return new $T(table, null)", fluent.query())
            .build();
    }

    private MethodSpec m_emptyQuery_table_Alias() {
        return super.publicMethod(M_EMPTY_QUERY, false, fluent.query())
            .addModifiers(Modifier.STATIC)
            .addParameter(CN_Supplier_Str, "table")
            .addParameter(String.class, "alias")
            .addStatement("return new $T(table, alias)", fluent.query())
            .build();
    }

    private MethodSpec m_defaultQuery() {
        return super.publicMethod(M_DEFAULT_QUERY, false, fluent.query())
            .addModifiers(Modifier.STATIC)
            .addStatement("return mapping.defaultQuery()")
            .build();
    }

    private MethodSpec m_aliasQuery_0() {
        return super.publicMethod(M_ALIAS_QUERY, false, fluent.query())
            .addModifiers(Modifier.STATIC)
            .addJavadoc(JavaDoc_Alias_Query_0)
            .addStatement("return mapping.aliasQuery()")
            .build();
    }

    private MethodSpec m_aliasQuery_1_String() {
        return super.publicMethod(M_ALIAS_QUERY, false, fluent.query())
            .addModifiers(Modifier.STATIC)
            .addParameter(String.class, "alias")
            .addJavadoc(JavaDoc_Alias_Query_1)
            .addStatement("return mapping.aliasQuery(alias)")
            .build();
    }

    private MethodSpec m_aliasWith_1_BaseQuery() {
        return super.publicMethod(M_ALIAS_WITH, false, fluent.query())
            .addParameter(BaseQuery.class, "fromQuery")
            .addModifiers(Modifier.STATIC)
            .addJavadoc(JavaDoc_Alias_With_1)
            .addStatement("return mapping.aliasWith(fromQuery)")
            .build();
    }

    private MethodSpec m_aliasWith_2_String_BaseQuery() {
        return super.publicMethod(M_ALIAS_WITH, false, fluent.query())
            .addParameter(String.class, "alias")
            .addParameter(BaseQuery.class, "fromQuery")
            .addModifiers(Modifier.STATIC)
            .addJavadoc(JavaDoc_Alias_With_2)
            .addStatement("return mapping.aliasWith(alias, fromQuery)")
            .build();
    }

    /**
     * public final Selector select = new Selector(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_select() {
        return FieldSpec.builder(fluent.selector(),
            "select", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("指定查询字段, 默认无需设置")
            .initializer("new $T(this)", fluent.selector())
            .build();
    }

    /**
     * public final GroupBy groupBy = new GroupBy(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_groupBy() {
        return FieldSpec.builder(fluent.groupBy(), "groupBy", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("分组：GROUP BY 字段, ...\n")
            .addJavadoc("例: groupBy('id', 'name')")
            .initializer("new $T(this)", fluent.groupBy())
            .build();
    }

    /**
     * public final GroupBy groupBy = new GroupBy(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_having() {
        return FieldSpec.builder(fluent.having(), "having", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("分组条件设置 having...")
            .initializer("new $T(this)", fluent.having())
            .build();
    }

    /**
     * public final GroupBy groupBy = new GroupBy(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_orderBy() {
        return FieldSpec.builder(fluent.queryOrderBy(), "orderBy", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("排序设置 order by ...")
            .initializer("new $T(this)", fluent.queryOrderBy())
            .build();
    }

    /**
     * public final QueryWhere where = new QueryWhere(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_where() {
        return FieldSpec.builder(fluent.queryWhere(), "where", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new $T(this)", fluent.queryWhere())
            .addJavadoc("查询条件 where ...")
            .build();
    }

    /**
     * public EntityQuery() {}
     *
     * @return MethodSpec
     */
    private MethodSpec constructor0() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("this(mapping.table(), null)")
            .build();
    }

    /**
     * public XyzQuery(String alias) {}
     *
     * @return MethodSpec
     */
    private MethodSpec constructor1_String() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "alias")
            .addStatement("this(mapping.table(), alias)")
            .build();
    }

    /**
     * public XyzQuery(String alias) {}
     *
     * @return MethodSpec
     */
    private MethodSpec constructor2_String_Parameter() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "alias")
            .addParameter(Parameters.class, "parameters")
            .addStatement("this(alias)")
            .addStatement("this.sharedParameter(parameters)")
            .build();
    }

    /**
     * public XyzQuery(Supplier&gt;String&lt; table, String alias) {}
     *
     * @return MethodSpec
     */
    private MethodSpec constructor2_String_String() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(CN_Supplier_Str, "table")
            .addParameter(String.class, "alias")
            .addStatement("super(table, alias, $T.class, $T.class)", fluent.entity(), fluent.query())
            .build();
    }

    /**
     * public QueryWhere where() {}
     *
     * @return MethodSpec
     */
    private MethodSpec m_where() {
        return super.publicMethod("where", true, fluent.queryWhere())
            .addStatement("return this.where")
            .build();
    }

    private ParameterizedTypeName superClass() {
        ClassName base = ClassName.get(BaseQuery.class);
        ClassName entity = fluent.entity();
        ClassName query = fluent.query();
        return ParameterizedTypeName.get(base, entity, query);
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}