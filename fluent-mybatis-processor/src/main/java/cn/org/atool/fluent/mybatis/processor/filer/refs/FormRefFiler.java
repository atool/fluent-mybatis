package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.functions.FormApply;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_NOT_FLUENT_MYBATIS_EXCEPTION;

/**
 * IMapperRef 文件构造
 *
 * @author darui.wu
 */
public class FormRefFiler extends AbstractFile {
    private static String FormRef = "FormRef";

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
        super.staticImport(builder);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_formSetter(fluent));
        }
    }

    private FieldSpec f_formSetter(FluentEntity fluent) {
        return FieldSpec.builder(
            parameterizedType(ClassName.get(FormApply.class),fluent.entity(), fluent.formSetter())
            , fluent.lowerNoSuffix(), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .initializer("$T::by", fluent.formSetter())
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