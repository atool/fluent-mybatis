package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_NOT_FLUENT_MYBATIS_EXCEPTION;
import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.PUBLIC_STATIC_FINAL;

/**
 * IMappingRef 文件构造
 *
 * @author darui.wu
 */
public class FieldRefFiler extends AbstractFile {
    private static final String FieldRef = "FieldRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), FieldRef);
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(MybatisUtil.class, M_NOT_FLUENT_MYBATIS_EXCEPTION);
        builder.skipJavaLangImports(true);
    }

    public FieldRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = FieldRef;
        this.comment = "Entity所有Mapping引用";
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addType(this.class_mapping(fluent));
        }
    }

    private TypeSpec class_mapping(FluentEntity fluent) {
        return TypeSpec.classBuilder(fluent.getNoSuffix())
            .addModifiers(PUBLIC_STATIC_FINAL)
            .superclass(fluent.entityMapping())
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