package cn.org.atool.fluent.processor.mybatis.filer.refs;

import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Ref;
import static cn.org.atool.fluent.processor.mybatis.filer.FilerKit.PUBLIC_STATIC;
import static cn.org.atool.fluent.processor.mybatis.filer.FilerKit.PUBLIC_STATIC_FINAL;

/**
 * Ref 文件构造
 *
 * @author darui.wu
 */
public class RefFiler extends AbstractFile {
    private final List<FluentEntity> fluents;

    public RefFiler(String basePackage, List<FluentEntity> fluents) {
        this.packageName = basePackage;
        this.fluents = fluents;
        this.klassName = Suffix_Ref;
        this.comment = "\n" +
            " o - 查询器，更新器工厂类单例引用\n" +
            " o - 应用所有Mapper Bean引用\n" +
            " o - Entity关联对象延迟加载查询实现";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addType(this.type_field())
            .addType(this.type_query());
    }

    private TypeSpec type_field() {
        TypeSpec.Builder spec = TypeSpec.interfaceBuilder("Field")
            .addJavadoc("所有Entity FieldMapping引用")
            .addModifiers(PUBLIC_STATIC);
        for (FluentEntity fluent : this.fluents) {
            spec.addType(this.type_mapping(fluent));
        }
        return spec.build();
    }

    private TypeSpec type_mapping(FluentEntity fluent) {
        return TypeSpec.classBuilder(fluent.getNoSuffix())
            .addModifiers(PUBLIC_STATIC_FINAL)
            .superclass(fluent.entityMapping())
            .build();
    }

    private TypeSpec type_query() {
        TypeSpec.Builder spec = TypeSpec.interfaceBuilder("Query")
            .addModifiers(PUBLIC_STATIC);
        for (FluentEntity fluent : this.fluents) {
            spec.addField(this.f_mapping(fluent));
        }
        return spec.build();
    }

    private FieldSpec f_mapping(FluentEntity fluent) {
        return FieldSpec.builder(fluent.entityMapping(), fluent.lowerNoSuffix(), PUBLIC_STATIC_FINAL)
            .initializer("$T.MAPPING", fluent.entityMapping())
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