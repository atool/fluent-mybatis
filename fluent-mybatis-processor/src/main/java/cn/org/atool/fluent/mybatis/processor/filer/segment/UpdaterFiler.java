package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Wrapper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Update;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
import static cn.org.atool.generator.util.ClassNames.CN_List_Str;

/**
 * updater代码生成
 *
 * @author wudarui
 */
public class UpdaterFiler extends AbstractFiler {
    public UpdaterFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "更新构造";
    }

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Update;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Wrapper);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(If.class, "notBlank");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(this.superKlass())
            .addField(this.f_update())
            .addField(this.f_setter())
            .addField(this.f_where())
            .addField(this.f_orderBy())
            .addMethod(this.constructor0())
            .addMethod(this.m_where())
            .addMethod(this.m_primary())
            .addMethod(this.m_allFields())
            .addMethod(this.m_emptyUpdater())
            .addMethod(this.m_defaultUpdater())
        ;
    }

    /**
     * public final UpdateSetter update = new UpdateSetter(this);
     *
     * @return
     */
    private FieldSpec f_update() {
        return FieldSpec.builder(fluent.updateSetter(),
            "update", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new UpdateSetter(this)")
            .addJavadoc("same as {@link #set}")
            .build();
    }

    private FieldSpec f_setter() {
        return FieldSpec.builder(fluent.updateSetter(),
            "set", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("update")
            .addJavadoc("same as {@link #update}")
            .build();
    }

    /**
     * public final UpdateWhere where = new UpdateWhere(this);
     *
     * @return
     */
    private FieldSpec f_where() {
        return FieldSpec.builder(fluent.updateWhere(),
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
        return FieldSpec.builder(fluent.updateOrderBy(),
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
                fluent.mapping(),
                fluent.entity(),
                fluent.query()
            )
            .build();
    }

    private ParameterizedTypeName superKlass() {
        ClassName base = ClassName.get(BaseUpdate.class);
        ClassName entity = fluent.entity();
        ClassName updater = fluent.updater();
        ClassName query = fluent.query();
        return ParameterizedTypeName.get(base, entity, updater, query);
    }

    /**
     * public QueryWhere where() {}
     *
     * @return
     */
    private MethodSpec m_where() {
        return super.publicMethod("where", true, fluent.updateWhere())
            .addStatement("return this.where")
            .build();
    }

    private MethodSpec m_emptyUpdater() {
        return super.publicMethod(M_NEW_UPDATER, false, fluent.updater())
            .addModifiers(Modifier.STATIC)
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    private MethodSpec m_defaultUpdater() {
        return super.publicMethod(M_DEFAULT_UPDATER, false, fluent.updater())
            .addModifiers(Modifier.STATIC)
            .addStatement("return $T.INSTANCE.defaultUpdater()", fluent.defaults())
            .build();
    }

    private MethodSpec m_allFields() {
        return MethodSpec.methodBuilder("allFields")
            .addModifiers(Modifier.PROTECTED)
            .returns(CN_List_Str)
            .addStatement("return $T.ALL_COLUMNS", fluent.mapping())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}