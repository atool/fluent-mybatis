package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNameConst.Pack_Wrapper;
import static cn.org.atool.fluent.mybatis.entity.base.ClassNameConst.Suffix_Query;

public class QueryGenerator extends AbstractGenerator {

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + Suffix_Query;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Wrapper);
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    public QueryGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
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
        return FieldSpec.builder(WrapperHelperGenerator.selector(fluentEntityInfo),
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
        return FieldSpec.builder(WrapperHelperGenerator.groupBy(fluentEntityInfo), "groupBy", Modifier.PUBLIC, Modifier.FINAL)
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
        return FieldSpec.builder(WrapperHelperGenerator.having(fluentEntityInfo), "having", Modifier.PUBLIC, Modifier.FINAL)
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
        return FieldSpec.builder(WrapperHelperGenerator.queryOrderBy(fluentEntityInfo), "orderBy", Modifier.PUBLIC, Modifier.FINAL)
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
        return FieldSpec.builder(WrapperHelperGenerator.queryWhere(fluentEntityInfo), "where", Modifier.PUBLIC, Modifier.FINAL)
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
                MappingGenerator.className(fluentEntityInfo),
                fluentEntityInfo.className(),
                QueryGenerator.className(fluentEntityInfo)
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
                MappingGenerator.className(fluentEntityInfo),
                fluentEntityInfo.className(),
                QueryGenerator.className(fluentEntityInfo)
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
            .returns(QueryGenerator.className(fluentEntityInfo));
        if (fluentEntityInfo.getPrimary() == null) {
            builder.addStatement("throw new $T($S + $T.Table_Name + $S)",
                FluentMybatisException.class, "The primary key of in table[",
                MappingGenerator.className(fluentEntityInfo), "] was not found.");
        } else {
            builder.addStatement("return this.select($T.$L.column)",
                MappingGenerator.className(fluentEntityInfo), fluentEntityInfo.getPrimary().getProperty());
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
            .returns(WrapperHelperGenerator.queryWhere(fluentEntityInfo))
            .addStatement("return this.where")
            .build();
    }

    private ParameterizedTypeName superKlass() {
        ClassName base = ClassName.get(BaseQuery.class);
        ClassName entity = fluentEntityInfo.className();
        ClassName query = QueryGenerator.className(fluentEntityInfo);
        return ParameterizedTypeName.get(base, entity, query);
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}