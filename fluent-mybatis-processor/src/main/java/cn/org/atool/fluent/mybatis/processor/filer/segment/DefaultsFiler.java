package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Helper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Defaults;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.FM_BaseDefault;

/**
 * 构造Query和Updater的工程类
 *
 * @author darui.wu
 */
public class DefaultsFiler extends AbstractFiler {
    public DefaultsFiler(FluentEntity fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = getClassName(fluent);
    }

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Defaults;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Helper);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(fluent.mapping(), "Table_Name");
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(parameterizedType(FM_BaseDefault, fluent.entity(), fluent.query(), fluent.updater(), fluent.defaults()))
            .addSuperinterface(ClassNames2.getClassName(fluent.getDefaults()))
            .addField(FieldSpec.builder(fluent.defaults(), "INSTANCE", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", fluent.defaults())
                .build())
            .addMethod(this.m_constructor())
            .addMethod(this.m_emptyQuery())
            .addMethod(this.m_emptyUpdater())
            .addMethod(this.m_aliasQuery_2())
        ;
    }

    private MethodSpec m_constructor() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addStatement("super(Table_Name, $S, $T.$L)", fluent.getSchema(), DbType.class, fluent.getDbType().name())
            .build();
    }

    private MethodSpec m_emptyQuery() {
        return super.publicMethod(M_NEW_QUERY, true, fluent.query())
            .addStatement("return new $T()", fluent.query())
            .build();
    }

    private MethodSpec m_emptyUpdater() {
        return super.publicMethod(M_NEW_UPDATER, true, fluent.updater())
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    private MethodSpec m_aliasQuery_2() {
        return super.publicMethod(M_ALIAS_QUERY, true, fluent.query())
            .addJavadoc(JavaDoc_Alias_Query_0)
            .addParameter(String.class, "alias")
            .addParameter(Parameters.class, "parameters")
            .addStatement("return new $T(alias, parameters)", fluent.query())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}