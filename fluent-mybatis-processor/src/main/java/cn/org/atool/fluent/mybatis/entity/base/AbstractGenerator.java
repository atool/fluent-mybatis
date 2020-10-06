package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.generator.MappingGenerator;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractGenerator {
    protected TypeElement curElement;

    protected FluentEntityInfo fluent;

    protected String packageName;

    protected String klassName;

    protected String comment;

    public AbstractGenerator(TypeElement curElement, FluentEntityInfo fluent) {
        this.curElement = curElement;
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

    protected CodeBlock of(String format, Object... args) {
        return CodeBlock.of(format.replace('\'', '"'), args);
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
    protected MethodSpec m_hasPrimary() {
        return MethodSpec.methodBuilder("hasPrimary")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .returns(TypeVariableName.get("boolean"))
            .addStatement("return $L", fluent.getPrimary() != null)
            .build();
    }

    /**
     * protected void validateColumn(String column) throws FluentMybatisException
     *
     * @return
     */
    protected MethodSpec m_validateColumn() {
        return MethodSpec.methodBuilder("validateColumn")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Override.class)
            .addException(FluentMybatisException.class)
            .addParameter(String.class, "column")
            .addCode("if (notBlank(column) && !$T.ALL_COLUMNS.contains(column)) {\n", MappingGenerator.className(fluent))
            .addCode(this.of(
                "\tthrow new FluentMybatisException('the column[' + column + '] was not found in table[' + $T.Table_Name + '].');\n",
                MappingGenerator.className(fluent)
            ))
            .addCode("}")
            .build();
    }

    /**
     * 定义方式如下的方法
     * <pre>
     * @Override
     * public abstract Xyz methodName(...);
     * </pre>
     *
     * @param methodName
     * @return
     */
    protected MethodSpec.Builder sqlMethod(String methodName) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName);
        //builder.addAnnotation(Override.class);
        builder.returns(String.class);
        builder.addModifiers(Modifier.PUBLIC);
        return builder;
    }
}