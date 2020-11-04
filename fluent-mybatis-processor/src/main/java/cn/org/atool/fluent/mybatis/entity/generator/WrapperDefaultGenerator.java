package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.entity.base.MethodName.M_DEFAULT_QUERY;
import static cn.org.atool.fluent.mybatis.entity.base.MethodName.M_DEFAULT_UPDATER;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Helper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_WrapperDefault;

/**
 * 构造Query和Updater的工程类
 *
 * @author darui.wu
 */
public class WrapperDefaultGenerator extends AbstractGenerator {
    public WrapperDefaultGenerator(FluentEntityInfo fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = getClassName(fluent);
    }

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + Suffix_WrapperDefault;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Helper);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        this.addWrapperDefault(builder, fluent.getDefaults());
        builder.addField(FieldSpec.builder(fluent.wrapperFactory(), "INSTANCE", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("new $T()", fluent.wrapperFactory())
            .build());
        builder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());
        builder.addMethod(this.m_newQuery_0());
        builder.addMethod(this.m_newQuery_1());
        builder.addMethod(this.m_newQuery_2());
        builder.addMethod(this.m_newUpdater());
    }

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

    private MethodSpec m_newQuery_0() {
        return super.publicMethod(M_DEFAULT_QUERY, false, fluent.query())
            .addJavadoc("实例化查询构造器\n")
            .addJavadoc("o - 设置默认查询条件\n\n")
            .addJavadoc("@return 查询构造器")
            .addStatement("$T query = new $T()", fluent.query(), fluent.query())
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_newQuery_1() {
        return super.publicMethod(M_DEFAULT_QUERY, false, fluent.query())
            .addParameter(String.class, "alias")
            .addJavadoc("实例化查询构造器\n")
            .addJavadoc("o - 设置默认查询条件\n")
            .addJavadoc("o - 设置别名alias\n\n")
            .addJavadoc("@param alias 别名\n")
            .addJavadoc("@return 查询构造器")
            .addStatement("$T query = new $T(alias, new $T())", fluent.query(), fluent.query(), ClassName.get(Parameters.class))
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_newQuery_2() {
        return super.publicMethod(M_DEFAULT_QUERY, false, fluent.query())
            .addParameter(String.class, "alias")
            .addParameter(BaseQuery.class, "joinFrom")
            .addJavadoc("实例化查询构造器\n")
            .addJavadoc("o - 设置默认查询条件\n")
            .addJavadoc("o - 设置别名alias\n")
            .addJavadoc("o - 设置变量实例来自From查询实例\n\n")
            .addJavadoc("@param alias 别名\n")
            .addJavadoc("@param joinFrom 关联查询时,from表查询对象\n")
            .addJavadoc("@return 查询构造器")
            .addStatement("$T query = new $T(alias, joinFrom.getWrapperData().getParameters())", fluent.query(), fluent.query())
            .addStatement("this.setQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_newUpdater() {
        return super.publicMethod(M_DEFAULT_UPDATER, false, fluent.updater())
            .addJavadoc("实例化更新构造器\n")
            .addJavadoc("o - 设置默认更新条件\n\n")
            .addJavadoc("@return 更新构造器")
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