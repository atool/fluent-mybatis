package cn.org.atool.fluent.processor.mybatis.filer.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.processor.mybatis.base.FluentClassName;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.processor.mybatis.filer.AbstractFiler;
import cn.org.atool.fluent.processor.mybatis.filer.FilerKit;
import cn.org.atool.fluent.mybatis.segment.fragment.Fragments;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import com.palantir.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.common.kits.StringKit.PRE_SET;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.processor.mybatis.base.MethodName.M_DEFAULT_UPDATER;
import static cn.org.atool.fluent.processor.mybatis.base.MethodName.M_EMPTY_UPDATER;

/**
 * updater代码生成
 *
 * @author wudarui
 */
public class UpdaterFiler extends AbstractFiler {
    /**
     * 构造函数
     *
     * @param fluentEntity FluentEntity
     */
    public UpdaterFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "更新构造";
    }

    /**
     * 获取类名
     *
     * @param fluentEntity FluentClassName
     * @return class name
     */
    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Update;
    }

    /**
     * 获取包名
     *
     * @param fluentEntity FluentClassName
     * @return package name
     */
    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Wrapper);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(If.class, "notBlank");
        spec.addStaticImport(fluent.entityMapping(), Suffix_MAPPING);
        spec.addStaticImport(Fragments.class, "fragment");
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(this.superKlass())
                .addField(this.f_setter())
                .addField(this.f_where())
                .addField(this.f_orderBy());
        /* method */
        spec.addMethod(this.constructor0())
                .addMethod(this.constructor4_Default_Table_Alias_Parameter())
                .addMethod(this.m_where())
                .addMethod(this.m_mapping());
        /* static method */
        spec.addMethod(this.m_emptyUpdater())
                .addMethod(this.m_emptyUpdater_table())
                .addMethod(this.m_updater())
                .addMethod(this.m_defaultUpdater())
                .addMethod(this.m_updater_table());
    }

    private FieldSpec f_setter() {
        return FieldSpec.builder(fluent.updateSetter(),
                PRE_SET, Modifier.PUBLIC, Modifier.FINAL)
                .initializer("new $T(this)", fluent.updateSetter())
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
                .addStatement("this(true, null, null, null)")
                .build();
    }

    private MethodSpec constructor4_Default_Table_Alias_Parameter() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(boolean.class, "defaults")
                .addParameter(IFragment.class, "table")
                .addParameter(StringSupplier.class, "alias")
                .addParameter(Parameters.class, "shared")
                .addStatement("super(table, alias, $T.class)", fluent.entity())
                .beginControlFlow("if(shared != null)")
                .addStatement("this.sharedParameter(shared)")
                .endControlFlow()
                .beginControlFlow("if (defaults)")
                .addStatement("$L.defaultSetter().setUpdateDefault(this)", Suffix_MAPPING)
                .endControlFlow()
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
        return FilerKit.publicMethod("where", fluent.updateWhere())
                .addStatement("return this.where")
                .build();
    }

    private MethodSpec m_emptyUpdater() {
        return FilerKit.staticMethod(M_EMPTY_UPDATER, fluent.updater())
                .addStatement("return new $T(false, null, null, null)", fluent.updater())
                .build();
    }

    private MethodSpec m_emptyUpdater_table() {
        return FilerKit.staticMethod(M_EMPTY_UPDATER, fluent.updater())
                .addParameter(StringSupplier.class, "table")
                .addStatement("return new $T(false, fragment(table), null, null)", fluent.updater())
                .build();
    }

    private MethodSpec m_updater() {
        return FilerKit.staticMethod(M_DEFAULT_UPDATER, fluent.updater())
                .addStatement("return new $T(true, null, null, null)", fluent.updater())
                .build();
    }

    private MethodSpec m_defaultUpdater() {
        return FilerKit.staticMethod("defaultUpdater", fluent.updater())
                .addStatement("return updater()")
                .build();
    }

    private MethodSpec m_updater_table() {
        return FilerKit.staticMethod(M_DEFAULT_UPDATER, fluent.updater())
                .addParameter(StringSupplier.class, "table")
                .addStatement("return new $T(true, fragment(table), null, null)", fluent.updater())
                .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}