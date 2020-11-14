package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.entity.IEntity;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Helper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Defaults;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;

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
    protected void build(TypeSpec.Builder spec) {
        this.addWrapperDefault(spec, fluent.getDefaults());
        spec.addSuperinterface(IDefaultGetter.class)
            .addField(FieldSpec.builder(fluent.wrapperFactory(), "INSTANCE", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", fluent.wrapperFactory())
                .build())
            .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
            .addMethod(this.m_setEntityByDefault())
            .addMethod(this.m_defaultQuery())
            .addMethod(this.m_newUpdater())
            .addMethod(this.m_aliasQuery_0())
            .addMethod(this.m_aliasQuery_1())
            .addMethod(this.m_aliasWith_1())
            .addMethod(this.m_aliasWith_2());
    }

    /**
     * 设置implements自定义接口
     *
     * @param builder
     * @param daoInterface
     */
    protected void addWrapperDefault(TypeSpec.Builder builder, String daoInterface) {
        int dot = daoInterface.lastIndexOf('.');
        String packageName = "";
        String simpleClassName = daoInterface;
        if (dot > 0) {
            packageName = daoInterface.substring(0, dot);
            simpleClassName = daoInterface.substring(dot + 1);
        }
        builder.addSuperinterface(ClassName.get(packageName, simpleClassName));
    }

    private MethodSpec m_setEntityByDefault() {
        return super.publicMethod(M_SET_ENTITY_BY_DEFAULT, true, (TypeName) null)
            .addParameter(IEntity.class, "entity")
            .addStatement("this.setInsertDefault(entity)")
            .build();
    }

    private MethodSpec m_defaultQuery() {
        return super.publicMethod(M_DEFAULT_QUERY, true, fluent.query())
            .addStatement("$T query = new $T()", fluent.query(), fluent.query())
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_aliasQuery_0() {
        return super.publicMethod(M_ALIAS_QUERY, true, fluent.query())
            .addJavadoc(JavaDoc_Alias_Query_0)
            .addStatement("$T parameters = new Parameters()", Parameters.class)
            .addStatement("$T query = new $T(parameters.alias(), parameters)", fluent.query(), fluent.query())
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_aliasQuery_1() {
        return super.publicMethod(M_ALIAS_QUERY, true, fluent.query())
            .addParameter(String.class, "alias")
            .addJavadoc(JavaDoc_Alias_Query_1)
            .addStatement("$T query = new $T(alias, new $T())", fluent.query(), fluent.query(), ClassName.get(Parameters.class))
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_aliasWith_1() {
        return super.publicMethod(M_ALIAS_WITH, true, fluent.query())
            .addParameter(BaseQuery.class, "fromQuery")
            .addJavadoc(JavaDoc_Alias_With_1)
            .addStatement("$T parameters = fromQuery.getWrapperData().getParameters()", Parameters.class)
            .addStatement("$T query = new $T(parameters.alias(), parameters)", fluent.query(), fluent.query())
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_aliasWith_2() {
        return super.publicMethod(M_ALIAS_WITH, true, fluent.query())
            .addParameter(String.class, "alias")
            .addParameter(BaseQuery.class, "fromQuery")
            .addJavadoc(JavaDoc_Alias_With_2)
            .addStatement("$T query = new $T(alias, fromQuery.getWrapperData().getParameters())", fluent.query(), fluent.query())
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_newUpdater() {
        return super.publicMethod(M_DEFAULT_UPDATER, true, fluent.updater())
            .addStatement("$T updater = new $T()", fluent.updater(), fluent.updater())
            .addStatement("this.setUpdateDefault(updater)")
            .addStatement("return updater")
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}