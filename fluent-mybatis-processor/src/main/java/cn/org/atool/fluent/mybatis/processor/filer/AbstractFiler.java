package cn.org.atool.fluent.mybatis.processor.filer;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NEWLINE;

@SuppressWarnings({"rawtypes", "UnusedReturnValue"})
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
     * @return ignore
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

    protected void staticImport(JavaFile.Builder spec) {
    }

    /**
     * 代码块, 或者注释块
     *
     * @param lines 代码行
     * @return ignore
     */
    protected CodeBlock codeBlock(String... lines) {
        return CodeBlock.join(Stream.of(lines).map(CodeBlock::of).collect(Collectors.toList()), NEWLINE);
    }

    protected CodeBlock codeBlock(CodeBlock... blocks) {
        return CodeBlock.join(Stream.of(blocks).collect(Collectors.toList()), NEWLINE);
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
     * @return ignore
     */
    protected abstract boolean isInterface();

    /**
     * protected boolean hasPrimary()
     *
     * @return ignore
     */
    protected MethodSpec m_mapping() {
        return this.protectedMethod("mapping", true, ClassName.get(IMapping.class))
            .addStatement("return $T.MAPPING", fluent.mapping())
            .build();
    }

    protected MethodSpec.Builder publicMethod(String methodName, boolean isOverride, Class returnKlass) {
        return this.publicMethod(methodName, isOverride, returnKlass == null ? null : ClassName.get(returnKlass));
    }

    /**
     * 定义方式如下的方法
     * <pre>
     * public abstract Xyz methodName(...);
     * </pre>
     *
     * @param methodName name of method
     * @param isOverride 是否注解@Override
     * @return ignore
     */
    protected MethodSpec.Builder publicMethod(String methodName, boolean isOverride, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        if (isOverride) {
            builder.addAnnotation(Override.class);
        }
        if (returnKlass != null) {
            builder.returns(returnKlass);
        }
        builder.addModifiers(Modifier.PUBLIC);
        return builder;
    }

    protected MethodSpec.Builder protectedMethod(String methodName, boolean isOverride, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        if (isOverride) {
            builder.addAnnotation(Override.class);
        }
        if (returnKlass != null) {
            builder.returns(returnKlass);
        }
        builder.addModifiers(Modifier.PROTECTED);
        return builder;
    }

    /**
     * 未定义主键异常
     *
     * @param builder MethodSpec.Builder
     * @return ignore
     */
    public static MethodSpec.Builder throwPrimaryNoFound(MethodSpec.Builder builder) {
        return builder.addStatement("throw new $T($S)", RuntimeException.class, "primary key not found.");
    }

    protected FieldSpec f_defaults() {
        return FieldSpec.builder(fluent.defaults(),
            "defaults", Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("默认设置器")
            .initializer("$T.defaults", fluent.wrapperHelper())
            .build();
    }
}