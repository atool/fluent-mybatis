package cn.org.atool.fluent.mybatis.processor.form;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.filer.FormFieldInfo;
import cn.org.atool.fluent.form.meta.FormFieldMeta;
import cn.org.atool.fluent.form.meta.IFormMeta;
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
    private final List<FormFieldInfo> fields;

    public FormMetaFiler(ClassName className, List<FormFieldInfo> fields) {
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
            .addSuperinterface(IFormMeta.class)
            .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class).addMember("value", "{$S}", "unused").build())
            .addMethod(this.m_findFormMetas());

        return JavaFile.builder(this.className.packageName(), type.build())
            .addStaticImport(EntryType.class, "*")
            .skipJavaLangImports(true)
            .build();
    }

    private MethodSpec m_findFormMetas() {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("findFormMetas")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .returns(ParameterizedTypeName.get(List.class, FormFieldMeta.class))
            .addAnnotation(Override.class)
            .addStatement("List<FormFieldMeta> metas = new $T<>()", ArrayList.class);
        for (FormFieldInfo f : this.fields) {
            spec.addStatement("this.add(metas, $S, $L, $S, $T::$L, $S, $T::$L, $L)",
                f.getEntryName(), f.getEntryType(),
                f.getterName(), className, f.getterName(),
                f.setterName(), className, f.setterName(),
                f.isIgnoreNull()
            );
        }
        return spec.addStatement("return metas").build();
    }
}