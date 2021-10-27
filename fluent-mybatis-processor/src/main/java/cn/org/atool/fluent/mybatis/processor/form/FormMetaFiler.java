package cn.org.atool.fluent.mybatis.processor.form;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.List;

public class FormMetaFiler {
    private ClassName className;
    private List<FormFieldInfo> fields;

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
        TypeSpec.Builder builder = TypeSpec.classBuilder(this.className).addModifiers(Modifier.PUBLIC);
        builder.addJavadoc("@author powered by FluentMybatis");
        JavaFile.Builder javaBuilder = JavaFile.builder(this.className.packageName(), builder.build());
        javaBuilder.skipJavaLangImports(true);
        return javaBuilder.build();
    }
}