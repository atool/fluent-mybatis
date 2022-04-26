package cn.org.atool.fluent.processor.mybatis.filer;

import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_MAPPING;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_mapping;
import static cn.org.atool.fluent.processor.mybatis.filer.ClassNames2.CN_Optional_IMapping;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.NEWLINE;

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
        return FilerKit.publicMethod(Suffix_mapping, CN_Optional_IMapping)
            .addStatement("return Optional.of($L)", Suffix_MAPPING)
            .build();
    }
}