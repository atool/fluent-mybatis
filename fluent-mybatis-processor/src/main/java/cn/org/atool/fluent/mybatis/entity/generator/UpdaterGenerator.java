package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;
import cn.org.atool.fluent.mybatis.entity.EntityKlass;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class UpdaterGenerator extends AbstractGenerator {
    public UpdaterGenerator(TypeElement curElement, EntityKlass entityKlass) {
        super(curElement, entityKlass);
        this.packageName = getPackageName(entityKlass);
        this.klassName = getClassName(entityKlass);
        this.comment = "更新构造";
    }

    public static String getClassName(EntityKlass entityKlass) {
        return entityKlass.getNoSuffix() + "Update";
    }

    public static String getPackageName(EntityKlass entityKlass) {
        return entityKlass.getPackageName("wrapper");
    }

    public static ClassName className(EntityKlass entityKlass) {
        return ClassName.get(getPackageName(entityKlass), getClassName(entityKlass));
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(MybatisUtil.class, "isNotBlank");
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
        return FieldSpec.builder(WrapperHelperGenerator.updateSetter(entityKlass),
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
        return FieldSpec.builder(WrapperHelperGenerator.updateWhere(entityKlass),
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
        return FieldSpec.builder(WrapperHelperGenerator.updateOrderBy(entityKlass),
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
                MappingGenerator.className(entityKlass),
                entityKlass.className(),
                QueryGenerator.className(entityKlass)
            )
            .build();
    }

    private ParameterizedTypeName superKlass() {
        ClassName base = ClassName.get(BaseUpdate.class);
        ClassName entity = entityKlass.className();
        ClassName updater = UpdaterGenerator.className(entityKlass);
        ClassName query = QueryGenerator.className(entityKlass);
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
            .returns(WrapperHelperGenerator.updateWhere(entityKlass))
            .addStatement("return this.where")
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}