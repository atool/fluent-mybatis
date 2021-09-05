package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_DEFAULT_UPDATER;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_EMPTY_UPDATER;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Supplier_Str;

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
        spec.addStaticImport(fluent.entityMapping(), Suffix_MAPPING);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(this.superKlass())
            .addField(this.f_setter())
            .addField(this.f_update())
            .addField(this.f_where())
            .addField(this.f_orderBy())
            .addMethod(this.constructor0())
            .addMethod(this.constructor2_supplier_string())
            .addMethod(this.m_where())
            .addMethod(this.m_mapping())
            .addMethod(this.m_emptyUpdater())
            .addMethod(this.m_emptyUpdater_table())
            .addMethod(this.m_defaultUpdater())
        ;
    }

    /**
     * public final UpdateSetter update = new UpdateSetter(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_update() {
        return FieldSpec.builder(fluent.updateSetter(),
            "update", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("set")
            .addJavadoc("replaced by {@link #set}")
            .addAnnotation(Deprecated.class)
            .build();
    }

    private FieldSpec f_setter() {
        return FieldSpec.builder(fluent.updateSetter(),
            "set", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new $T(this)", fluent.updateSetter())
            .addJavadoc("same as {@link #update}")
            .build();
    }

    /**
     * public final UpdateWhere where = new UpdateWhere(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_where() {
        return FieldSpec.builder(fluent.updateWhere(),
            "where", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new $T(this)", fluent.updateWhere())
            .build();
    }

    /**
     * public final UpdateOrderBy orderBy = new UpdateOrderBy(this);
     *
     * @return FieldSpec
     */
    private FieldSpec f_orderBy() {
        return FieldSpec.builder(fluent.updateOrderBy(),
            "orderBy", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new $T(this)", fluent.updateOrderBy())
            .build();
    }

    /**
     * public EntityUpdate() {}
     *
     * @return FieldSpec
     */
    private MethodSpec constructor0() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("this($L.table(), null)", Suffix_MAPPING)
            .build();
    }

    private MethodSpec constructor2_supplier_string() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(CN_Supplier_Str, "table")
            .addParameter(String.class, "alias")
            .addStatement("super(table, alias, $T.class, $T.class)", fluent.entity(), fluent.query())
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
     * public UpdateWhere where() {}
     *
     * @return FieldSpec
     */
    private MethodSpec m_where() {
        return super.publicMethod("where", true, fluent.updateWhere())
            .addStatement("return this.where")
            .build();
    }

    private MethodSpec m_emptyUpdater() {
        return super.publicMethod(M_EMPTY_UPDATER, false, fluent.updater())
            .addModifiers(Modifier.STATIC)
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    private MethodSpec m_emptyUpdater_table() {
        return super.publicMethod(M_EMPTY_UPDATER, false, fluent.updater())
            .addModifiers(Modifier.STATIC)
            .addParameter(CN_Supplier_Str, "table")
            .addStatement("return new $T(table, null)", fluent.updater())
            .build();
    }

    private MethodSpec m_defaultUpdater() {
        return super.publicMethod(M_DEFAULT_UPDATER, false, fluent.updater())
            .addModifiers(Modifier.STATIC)
            .addStatement("return $L.defaultUpdater()", Suffix_MAPPING)
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}