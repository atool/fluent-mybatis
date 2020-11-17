package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.FormSetter;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.model.FormQuery;
import cn.org.atool.fluent.mybatis.model.IFormQuery;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Helper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_EntityFormSetter;

public class FormSetterFiler extends AbstractFiler {

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_EntityFormSetter;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Helper);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(MybatisUtil.class, "assertNotNull");
        super.staticImport(spec);
    }

    public FormSetterFiler(FluentEntity fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = getClassName(fluent);
        this.comment = "Form Column Setter";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(super.parameterizedType(ClassName.get(FormSetter.class), fluent.formSetter()))
            .addSuperinterface(super.parameterizedType(
                fluent.segment(),
                super.parameterizedType(ClassName.get(IFormQuery.class), fluent.formSetter())
            ))
            .addMethod(this.constructor1())
            .addMethod(this.m_entityClass())
            .addMethod(this.m_byEntity())
            .addMethod(this.m_byMap())
        ;
    }

    private MethodSpec constructor1() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .build();
    }

    private MethodSpec m_entityClass() {
        return super.publicMethod("entityClass", true, Class.class)
            .addStatement("return $T.class", fluent.entity())
            .build();
    }

    private MethodSpec m_byEntity() {
        return super.publicMethod("byEntity", false,
            parameterizedType(ClassName.get(IFormQuery.class), fluent.formSetter()))
            .addModifiers(Modifier.STATIC)
            .addParameter(IEntity.class, "entity")
            .addStatement("assertNotNull($S, entity)", "entity")
            .addStatement("$T setter = new $T()", fluent.formSetter(), fluent.formSetter())
            .addStatement("$T q = $T.INSTANCE.defaultQuery()", IQuery.class, fluent.defaults())
            .addStatement("return setter.setQuery(new $T(entity, q, setter))", FormQuery.class)
            .build();
    }

    private MethodSpec m_byMap() {
        return super.publicMethod("byMap", false,
            parameterizedType(ClassName.get(IFormQuery.class), fluent.formSetter()))
            .addModifiers(Modifier.STATIC)
            .addParameter(Map.class, "map")
            .addStatement("assertNotNull($S, map)", "map")
            .addStatement("$T setter = new $T()", fluent.formSetter(), fluent.formSetter())
            .addStatement("$T q = $T.INSTANCE.defaultQuery()", IQuery.class, fluent.defaults())
            .addStatement("return setter.setQuery(new $T(q, map, setter))", FormQuery.class)
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}