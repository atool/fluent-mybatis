package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.functions.FormFunction;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_NOT_FLUENT_MYBATIS_EXCEPTION;
import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.PUBLIC_STATIC_FINAL;

/**
 * IMapperRef 文件构造
 *
 * @author darui.wu
 */
public class FormRefFiler extends AbstractFile {
    private static final String FormRef = "FormRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), FormRef);
    }

    public FormRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = FormRef;
        this.comment = "所有Entity Form Setter引用";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(MybatisUtil.class, M_NOT_FLUENT_MYBATIS_EXCEPTION);
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_formSetter(fluent));
        }
    }

    private FieldSpec f_formSetter(FluentEntity fluent) {
        TypeName cn = fluent.formSetter();
        return FieldSpec.builder(parameterizedType(ClassName.get(FormFunction.class), fluent.entity(), cn)
                , fluent.lowerNoSuffix(), PUBLIC_STATIC_FINAL)
            .addJavadoc("$T", fluent.wrapperHelper())
            .initializer("(obj, form) -> $T.by(obj, form)", cn)
            .build();
    }

    @Override
    protected boolean isInterface() {
        return true;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}