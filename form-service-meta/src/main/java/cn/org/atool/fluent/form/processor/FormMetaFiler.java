package cn.org.atool.fluent.form.processor;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.meta.EntryMeta;
import cn.org.atool.fluent.form.meta.EntryMetaKit;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成 XyzFormMeta java文件
 *
 * @author wudarui
 */
public class FormMetaFiler {
    private final ClassName className;
    private final List<FormField> fields;

    public FormMetaFiler(ClassName className, List<FormField> fields) {
        this.className = className;
        this.fields = fields;
    }

    /**
     * 生成java文件
     *
     * @return ignore
     */
    public final JavaFile javaFile() {
        ClassName metaKit = ClassName.get(this.className.packageName(), this.className.simpleName() + "MetaKit");
        TypeSpec.Builder type = TypeSpec.classBuilder(metaKit).addModifiers(Modifier.PUBLIC)
            .addJavadoc("$T\n@author powered by FluentMybatis", metaKit)
            .addSuperinterface(EntryMetaKit.class)
            .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "{$S}", "unused").build())
            .addField(this.f_entryMetas())
            .addStaticBlock(this.initMetas())
            .addMethod(this.m_entryMetas());

        return JavaFile.builder(this.className.packageName(), type.build())
            .addStaticImport(EntryType.class, "*")
            .skipJavaLangImports(true)
            .build();
    }

    private FieldSpec f_entryMetas() {
        return FieldSpec.builder(ParameterizedTypeName.get(List.class, EntryMeta.class), "metas")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .initializer("new $T<>($L)", ArrayList.class, this.fields.size())
            .build();
    }

    private CodeBlock initMetas() {
        CodeBlock.Builder spec = CodeBlock.builder();
        for (FormField f : this.fields) {
            spec.addStatement("metas.add(new EntryMeta($S, $L, $T::$L, $T::$L, $L))",
                f.getEntryName(), f.getEntryType(),
                className, f.getterName(),
                className, f.setterName(),
                f.isIgnoreNull()
            );
        }
        return spec.build();
    }

    private MethodSpec m_entryMetas() {
        return MethodSpec.methodBuilder("entryMetas")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .returns(ParameterizedTypeName.get(List.class, EntryMeta.class))
            .addAnnotation(Override.class)
            .addStatement("return metas")
            .build();
    }
}