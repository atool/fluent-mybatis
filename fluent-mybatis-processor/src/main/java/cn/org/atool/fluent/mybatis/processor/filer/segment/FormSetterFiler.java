package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseFormSetter;
import cn.org.atool.fluent.mybatis.functions.FormApply;
import cn.org.atool.fluent.mybatis.model.Form;
import cn.org.atool.fluent.mybatis.model.IFormApply;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.utility.PoJoHelper;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Helper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_EntityFormSetter;

public class FormSetterFiler extends AbstractFiler {

    public FormSetterFiler(FluentEntity fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = Suffix_EntityFormSetter;
        this.comment = "Form Column Setter";
    }

    public static String getPackageName(FluentClassName fluent) {
        return fluent.getPackageName(Pack_Helper) + "." + WrapperHelperFiler.getClassName(fluent);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        TypeName applyName = parameterizedType(ClassName.get(IFormApply.class), fluent.entity(), this.setterName());
        builder.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(BaseFormSetter.class)
            .addSuperinterface(super.parameterizedType(fluent.segment(), applyName))
            .addMethod(this.constructor1())
            .addMethod(this.m_entityClass())
            .addMethod(this.m_byObject())
        ;
    }

    private TypeVariableName setterName() {
        return TypeVariableName.get(Suffix_EntityFormSetter);
    }

    private MethodSpec constructor1() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(FormApply.class, "apply")
            .addStatement("super.formApply = apply")
            .build();
    }

    private MethodSpec m_entityClass() {
        return super.publicMethod("entityClass", true, Class.class)
            .addStatement("return $T.class", fluent.entity())
            .build();
    }

    private MethodSpec m_byObject() {
        return super.publicMethod("by", false,
            parameterizedType(ClassName.get(IFormApply.class), fluent.entity(), this.setterName()))
            .addModifiers(Modifier.STATIC)
            .addParameter(Object.class, "object")
            .addParameter(Form.class, "form")
            .addStatement("assertNotNull($S, object)", "object")
            .addStatement("$T map = $T.toMap(object)", Map.class, PoJoHelper.class)
            .addStatement("$T<FormApply, BaseFormSetter> apply = $L::new", Function.class, Suffix_EntityFormSetter)
            .addStatement("return new $T<>(apply, map, form)", FormApply.class)
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}