package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.entity.EntityKlass;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class QueryGenerator extends AbstractGenerator {

    public static String getClassName(EntityKlass entityKlass) {
        return entityKlass.getNoSuffix() + "Query";
    }

    public static String getPackageName(EntityKlass entityKlass) {
        return entityKlass.getPackageName("wrapper");
    }

    public static ClassName className(EntityKlass entityKlass) {
        return ClassName.get(getPackageName(entityKlass), getClassName(entityKlass));
    }

    public QueryGenerator(TypeElement curElement, EntityKlass entityKlass) {
        super(curElement, entityKlass);
        this.packageName = getPackageName(entityKlass);
        this.klassName = getClassName(entityKlass);
        this.comment = "查询构造";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(MybatisUtil.class, "isNotBlank");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(this.superKlass())
            .addField(this.f_select())
            .addField(this.f_groupBy())
            .addField(this.f_having())
            .addField(this.f_orderBy())
            .addField(this.f_where())
            .addMethod(this.constructor0())
            .addMethod(this.constructor1())
            .addMethod(this.m_selectId())
            .addMethod(this.m_where())
            .addMethod(this.m_hasPrimary())
            .addMethod(this.m_validateColumn());
    }

    /**
     * public final Selector select = new Selector(this);
     *
     * @return
     */
    private FieldSpec f_select() {
        return FieldSpec.builder(WrapperHelperGenerator.selector(entityKlass),
            "select", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("指定查询字段, 默认无需设置")
            .initializer("new Selector(this)")
            .build();
    }

    /**
     * public final GroupBy groupBy = new GroupBy(this);
     *
     * @return
     */
    private FieldSpec f_groupBy() {
        return FieldSpec.builder(WrapperHelperGenerator.groupBy(entityKlass), "groupBy", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("分组：GROUP BY 字段, ...\n")
            .addJavadoc("例: groupBy('id', 'name')")
            .initializer("new GroupBy(this)")
            .build();
    }

    /**
     * public final GroupBy groupBy = new GroupBy(this);
     *
     * @return
     */
    private FieldSpec f_having() {
        return FieldSpec.builder(WrapperHelperGenerator.having(entityKlass), "having", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("分组条件设置 having...")
            .initializer("new Having(this)")
            .build();
    }

    /**
     * public final GroupBy groupBy = new GroupBy(this);
     *
     * @return
     */
    private FieldSpec f_orderBy() {
        return FieldSpec.builder(WrapperHelperGenerator.queryOrderBy(entityKlass), "orderBy", Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("排序设置 order by ...")
            .initializer("new QueryOrderBy(this)")
            .build();
    }

    /**
     * public final QueryWhere where = new QueryWhere(this);
     *
     * @return
     */
    private FieldSpec f_where() {
        return FieldSpec.builder(WrapperHelperGenerator.queryWhere(entityKlass), "where", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new QueryWhere(this)")
            .addJavadoc("查询条件 where ...")
            .build();
    }

    /**
     * public EntityQuery() {}
     *
     * @return
     */
    private MethodSpec constructor0() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super($T.Table_Name, $T.class, $T.class)",
                MappingGenerator.className(entityKlass),
                entityKlass.className(),
                QueryGenerator.className(entityKlass)
            )
            .build();
    }

    /**
     * public AddressQuery(ParameterPair parameters) {}
     *
     * @return
     */
    private MethodSpec constructor1() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ClassName.get(ParameterPair.class), "parameters")
            .addStatement("super($T.Table_Name, parameters, $T.class, $T.class)",
                MappingGenerator.className(entityKlass),
                entityKlass.className(),
                QueryGenerator.className(entityKlass)
            )
            .build();
    }

    /**
     * public AddressQuery selectId() {}
     *
     * @return
     */
    private MethodSpec m_selectId() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("selectId")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(QueryGenerator.className(entityKlass));
        if (entityKlass.getPrimary() == null) {
            builder.addStatement("throw new $T($S + $T.Table_Name + $S)",
                FluentMybatisException.class, "The primary key of in table[",
                MappingGenerator.className(entityKlass), "] was not found.");
        } else {
            builder.addStatement("return this.select($T.$L.column)",
                MappingGenerator.className(entityKlass), entityKlass.getPrimary().getProperty());
        }
        return builder.build();
    }

    /**
     * public QueryWhere where() {}
     *
     * @return
     */
    private MethodSpec m_where() {
        return MethodSpec.methodBuilder("where")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(WrapperHelperGenerator.queryWhere(entityKlass))
            .addStatement("return this.where")
            .build();
    }

    private ParameterizedTypeName superKlass() {
        ClassName base = ClassName.get(BaseQuery.class);
        ClassName entity = entityKlass.className();
        ClassName query = QueryGenerator.className(entityKlass);
        return ParameterizedTypeName.get(base, entity, query);
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}