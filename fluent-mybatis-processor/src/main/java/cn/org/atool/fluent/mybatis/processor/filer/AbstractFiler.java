package cn.org.atool.fluent.mybatis.processor.filer;

import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractFiler {
    protected FluentEntity fluent;

    protected String packageName;

    protected String klassName;

    protected String comment;

    public AbstractFiler(FluentEntity fluent) {
        this.fluent = fluent;
    }

    /**
     * 生成java文件
     *
     * @return
     */
    public final JavaFile javaFile() {
        TypeSpec.Builder builder;
        if (this.isInterface()) {
            builder = TypeSpec.interfaceBuilder(klassName).addModifiers(Modifier.PUBLIC);
        } else {
            builder = TypeSpec.classBuilder(klassName).addModifiers(Modifier.PUBLIC);
        }
        this.build(builder);
        CodeBlock comment = this.codeBlock("",
            this.klassName + (this.comment == null ? "" : ": " + this.comment),
            "",
            "@author powered by FluentMybatis"
        );
        builder.addJavadoc(comment);
        JavaFile.Builder javaBuilder = JavaFile.builder(packageName, builder.build());
        this.staticImport(javaBuilder);
        return javaBuilder.build();
    }

    protected void staticImport(JavaFile.Builder builder) {
    }

    /**
     * 代码块, 或者注释块
     *
     * @param lines 代码行
     * @return
     */
    protected CodeBlock codeBlock(String... lines) {
        return CodeBlock.join(Stream.of(lines).map(CodeBlock::of).collect(Collectors.toList()), "\n");
    }

    protected CodeBlock codeBlock(CodeBlock... blocks) {
        return CodeBlock.join(Stream.of(blocks).collect(Collectors.toList()), "\n");
    }

    protected abstract void build(TypeSpec.Builder builder);

    protected TypeName parameterizedType(ClassName raw, TypeName... paras) {
        return ParameterizedTypeName.get(raw, paras);
    }

    protected TypeName parameterizedType(Class raw, Class... paras) {
        return ParameterizedTypeName.get(raw, paras);
    }

    /**
     * 是否接口类
     *
     * @return
     */
    protected abstract boolean isInterface();

    /**
     * protected boolean hasPrimary()
     *
     * @return
     */
    protected MethodSpec m_primary() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("primary")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .returns(String.class);
        if (fluent.getPrimary() == null) {
            builder.addStatement("return null");
        } else {
            builder.addStatement("return $T.$L.column", fluent.mapping(), fluent.getPrimary().getName());
        }
        return builder.build();
    }

    protected MethodSpec.Builder publicMethod(String methodName, boolean isOverride, Class returnKlass) {
        return this.publicMethod(methodName, isOverride, ClassName.get(returnKlass));
    }

    /**
     * 定义方式如下的方法
     * <pre>
     * @Override
     * public abstract Xyz methodName(...);
     * </pre>
     *
     * @param methodName
     * @param isOverride 是否注解@Override
     * @return
     */
    protected MethodSpec.Builder publicMethod(String methodName, boolean isOverride, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        if (isOverride) {
            builder.addAnnotation(Override.class);
        }
        builder.returns(returnKlass);
        builder.addModifiers(Modifier.PUBLIC);
        return builder;
    }

    protected MethodSpec.Builder protectedMethod(String methodName, boolean isOverride, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        if (isOverride) {
            builder.addAnnotation(Override.class);
        }
        builder.returns(returnKlass);
        builder.addModifiers(Modifier.PROTECTED);
        return builder;
    }

    /**
     * 未定义主键异常
     *
     * @param builder
     * @return
     */
    protected MethodSpec.Builder throwPrimaryNoFound(MethodSpec.Builder builder) {
        return builder.addStatement("throw new $T($S)",
            RuntimeException.class, "primary key not found.");
    }
}