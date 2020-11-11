package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QueryRefFiler extends AbstractFile {
    private static String QueryRef = "QueryRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), QueryRef);
    }

    public QueryRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = QueryRef;
        this.comment = "构造Entity对应的default query\n更新器工厂类单例引用";
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_factory(fluent));
        }
        spec.addField(this.f_allQuerySupplier())
            .addStaticBlock(this.m_initSupplier())
            .addMethod(m_defaultQuery(false));
    }

    private FieldSpec f_factory(FluentEntity fluent) {
        return FieldSpec.builder(fluent.wrapperFactory(), fluent.lowerNoSuffix(),
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$T.INSTANCE", fluent.wrapperFactory())
            .build();
    }

    public static MethodSpec m_defaultQuery(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("defaultQuery")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(IQuery.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("return $T.defaultQuery(clazz)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz实体对应的默认Query实例")
                .addCode("if (Supplier.containsKey(clazz)) {\n")
                .addStatement("\treturn Supplier.get(clazz).get()")
                .addCode("}\n")
                .addStatement("throw new $T($S + clazz.getName() + $S)",
                    RuntimeException.class, "the class[", "] is not a @FluentMybatis Entity or it's sub class.");
        }
        return spec.build();
    }

    private CodeBlock m_initSupplier() {
        List<CodeBlock> list = new ArrayList<>();
        for (FluentEntity fluent : FluentList.getFluents()) {
            list.add(CodeBlock.of("Supplier.put($T.class, $L::defaultQuery);\n", fluent.entity(), fluent.lowerNoSuffix()));
        }
        return CodeBlock.join(list, "");
    }

    private FieldSpec f_allQuerySupplier() {
        return FieldSpec.builder(parameterizedType(
            ClassName.get(Map.class),
            ClassName.get(Class.class),
            parameterizedType(Supplier.class, IQuery.class)
            ), "Supplier",
            Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .initializer("new $T<>()", HashMap.class)
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}