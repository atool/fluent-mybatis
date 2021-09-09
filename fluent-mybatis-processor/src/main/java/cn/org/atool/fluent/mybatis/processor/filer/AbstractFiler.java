package cn.org.atool.fluent.mybatis.processor.filer;

import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_MAPPING;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_mapping;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NEWLINE;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Optional_IMapping;

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
        javaBuilder.skipJavaLangImports(true);
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

    protected abstract void build(TypeSpec.Builder builder);

    protected TypeName paraType(ClassName raw, Object... paras) {
        TypeName[] types = typeNames(paras);
        return ParameterizedTypeName.get(raw, types);
    }

    protected TypeName paraType(Class raw, Object... paras) {
        TypeName[] types = typeNames(paras);
        return ParameterizedTypeName.get(ClassName.get(raw), types);
    }

    @SuppressWarnings("all")
    protected TypeName paraType(String typeName, Object... paras) {
        TypeName[] types = typeNames(paras);
        return ParameterizedTypeName.get(ClassNames2.getClassName(typeName), types);
    }

    private TypeName[] typeNames(Object... paras) {
        List<TypeName> types = new ArrayList<>(paras.length);
        for (Object p : paras) {
            if (p instanceof String) {
                types.add(TypeVariableName.get((String) p));
            } else if (p instanceof Class) {
                types.add(ClassName.get((Class) p));
            } else {
                types.add((TypeName) p);
            }
        }
        return types.toArray(new TypeName[0]);
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
        return this.publicMethod(Suffix_mapping, true, CN_Optional_IMapping)
            .addStatement("return Optional.of($L)", Suffix_MAPPING)
            .build();
    }

    protected MethodSpec.Builder publicMethod(String methodName, Class returnKlass) {
        return this.publicMethod(methodName, true, returnKlass == null ? null : ClassName.get(returnKlass));
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

    protected MethodSpec.Builder protectedMethod(String methodName, TypeName returnKlass) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        builder.addAnnotation(Override.class);
        if (returnKlass != null) {
            builder.returns(returnKlass);
        }
        builder.addModifiers(Modifier.PROTECTED);
        return builder;
    }

    @SuppressWarnings("all")
    protected AnnotationSpec suppressWarnings(String... values) {
        String format = Stream.of(values).map(s -> "$S")
            .collect(Collectors.joining(", ", "{", "}"));
        return AnnotationSpec.builder(SuppressWarnings.class)
            .addMember("value", format, values)
            .build();
    }

    public static Modifier[] PUBLIC_STATIC_FINAL = {Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL};

    public static Modifier[] PRIVATE_STATIC_FINAL = {Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL};
}