package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.Predicates;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.Pack_Wrapper;
import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.Suffix_Update;

public class UpdaterGenerator extends AbstractGenerator {
    public UpdaterGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "更新构造";
    }

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + Suffix_Update;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Wrapper);
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(Predicates.class, "notBlank");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(this.superKlass())
            .addField(this.f_setter())
            .addField(this.f_where())
            .addField(this.f_orderBy())
            .addMethod(this.constructor0())
            .addMethod(this.m_where())
            .addMethod(this.m_hasPrimary())
            .addMethod(this.m_validateColumn());
    }

    /**
     * public final UpdateSetter update = new UpdateSetter(this);
     *
     * @return
     */
    private FieldSpec f_setter() {
        return FieldSpec.builder(WrapperHelperGenerator.updateSetter(fluent),
            "update", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new UpdateSetter(this)")
            .build();
    }

    /**
     * public final UpdateWhere where = new UpdateWhere(this);
     *
     * @return
     */
    private FieldSpec f_where() {
        return FieldSpec.builder(WrapperHelperGenerator.updateWhere(fluent),
            "where", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new UpdateWhere(this)")
            .build();
    }

    /**
     * public final UpdateOrderBy orderBy = new UpdateOrderBy(this);
     *
     * @return
     */
    private FieldSpec f_orderBy() {
        return FieldSpec.builder(WrapperHelperGenerator.updateOrderBy(fluent),
            "orderBy", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new UpdateOrderBy(this)")
            .build();
    }

    /**
     * public EntityUpdate() {}
     *
     * @return
     */
    private MethodSpec constructor0() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super($T.Table_Name, $T.class, $T.class)",
                MappingGenerator.className(fluent),
                fluent.className(),
                QueryGenerator.className(fluent)
            )
            .build();
    }

    private ParameterizedTypeName superKlass() {
        ClassName base = ClassName.get(BaseUpdate.class);
        ClassName entity = fluent.className();
        ClassName updater = UpdaterGenerator.className(fluent);
        ClassName query = QueryGenerator.className(fluent);
        return ParameterizedTypeName.get(base, entity, updater, query);
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
            .returns(WrapperHelperGenerator.updateWhere(fluent))
            .addStatement("return this.where")
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}