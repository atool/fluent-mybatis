package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.FormSetter;
import cn.org.atool.fluent.mybatis.model.IFormQuery;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_NOT_FLUENT_MYBATIS_EXCEPTION;

/**
 * IMapperRef 文件构造
 *
 * @author darui.wu
 */
public class SetterRefFiler extends AbstractFile {
    private static String SetterRef = "SetterRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), SetterRef);
    }

    public SetterRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = SetterRef;
        this.comment = "所有Entity Form Setter引用";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(MybatisUtil.class, M_NOT_FLUENT_MYBATIS_EXCEPTION);
        super.staticImport(builder);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_formSetter(fluent));
        }
        spec.addField(this.f_setter())
            .addStaticBlock(this.m_initSetters())
            .addMethod(m_newFormSetter(false));
    }

    private CodeBlock m_initSetters() {
        List<CodeBlock> list = new ArrayList<>();
        for (FluentEntity fluent : FluentList.getFluents()) {
            list.add(CodeBlock.of("setters.put($L, $T.FormSetter::new);\n", fluent.lowerNoSuffix(), fluent.wrapperHelper()));
        }
        return CodeBlock.join(list, "");
    }

    private FieldSpec f_setter() {
        return FieldSpec.builder(
            parameterizedType(ClassName.get(Map.class), ClassName.get(Class.class), parameterizedType(Function.class, IFormQuery.class, FormSetter.class))
            , "setters", Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL)
            .initializer("new $T<>()", HashMap.class)
            .build();
    }

    private FieldSpec f_formSetter(FluentEntity fluent) {
        return FieldSpec.builder(
            parameterizedType(
                ClassName.get(Class.class),
                TypeVariableName.get(fluent.getNoSuffix() + "WrapperHelper.FormSetter"))
            , fluent.lowerNoSuffix(), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .initializer("$T.FormSetter.class", fluent.wrapperHelper())
            .build();
    }


    public static MethodSpec m_newFormSetter(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("newFormSetter")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "setterClass")
            .addParameter(IFormQuery.class, "query")
            .returns(FormSetter.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("return $T.newFormSetter(setterClass, query)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addCode("if (setters.containsKey(setterClass)) {\n")
                .addStatement("\treturn setters.get(setterClass).apply(query)")
                .addCode("}\n")
                .addStatement("throw $L(setterClass)", M_NOT_FLUENT_MYBATIS_EXCEPTION);
        }
        return spec.build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}