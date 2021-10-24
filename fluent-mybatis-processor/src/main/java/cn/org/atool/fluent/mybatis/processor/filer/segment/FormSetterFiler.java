package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.form.setter.BaseFormSetter;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.form.setter.FormApply;
import cn.org.atool.fluent.mybatis.form.Form;
import cn.org.atool.fluent.mybatis.form.setter.IFormApply;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.FilerKit;
import cn.org.atool.fluent.mybatis.utility.PoJoHelper;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

public class FormSetterFiler extends AbstractFiler {

    public FormSetterFiler(FluentEntity fluent) {
        super(fluent);
        this.packageName = getPackageName(fluent);
        this.klassName = Suffix_EntityFormSetter;
        this.comment = "Form Column Setter";
    }

    public static String getPackageName(FluentClassName fluent) {
        return fluent.getPackageName(Pack_Helper) + "." + SegmentFiler.getClassName(fluent);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        TypeName applyName = paraType(ClassName.get(IFormApply.class), fluent.entity(), this.setterName());
        builder.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(BaseFormSetter.class)
            .addSuperinterface(super.paraType(fluent.segment(), applyName))
            .addMethod(this.constructor1())
            .addMethod(this.m_mapping())
            .addMethod(this.m_entityClass())
            .addMethod(this.m_byObject())
        ;
    }

    @Override
    protected MethodSpec m_mapping() {
        return FilerKit.publicMethod("_" + Suffix_mapping, IMapping.class)
            .addStatement("return $L", Suffix_MAPPING)
            .build();
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
        return FilerKit.publicMethod(F_Entity_Class, Class.class)
            .addStatement("return $T.class", fluent.entity())
            .build();
    }

    private MethodSpec m_byObject() {
        return FilerKit.staticMethod("by", paraType(ClassName.get(IFormApply.class), fluent.entity(), this.setterName()))
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