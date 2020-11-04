package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.EntityLazyQuery;
import cn.org.atool.fluent.mybatis.entity.FluentEntity;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Autowired;
import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Getter;

/**
 * Mappers 代码生成
 *
 * @author darui.wu
 */
public class MappersFile extends AbstractFile {

    private static String Mappers = "Mappers";

    public static ClassName getClassName() {
        return ClassName.get(FluentEntity.getSamePackage(), Mappers);
    }

    public MappersFile() {
        this.packageName = FluentEntity.getSamePackage();
        this.klassName = Mappers;
        this.comment = "应用查询器，更新器工厂类";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(EntityLazyQuery.class);
        builder.addModifiers(Modifier.ABSTRACT);
        for (FluentEntity fluent : FluentEntity.getFluents()) {
            builder.addField(this.m_factory(fluent));
        }
        builder.addMethod(this.m_instance());
        for (FluentEntity fluent : FluentEntity.getFluents()) {
            builder.addField(this.m_mapper(fluent));
        }
    }

    private MethodSpec m_instance() {
        return MethodSpec.methodBuilder("INSTANCE")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .addJavadoc("Wrapper单例")
            .returns(MappersFile.getClassName())
            .addStatement("return ($L) $T.query()", Mappers, EntityLazyQuery.class)
            .build();
    }

    private FieldSpec m_mapper(FluentEntity fluent) {
        return FieldSpec.builder(fluent.mapper(), fluent.lowerNoSuffix() + "Mapper",
            Modifier.PROTECTED)
            .addAnnotation(CN_Getter)
            .addAnnotation(CN_Autowired)
            .build();
    }

    private FieldSpec m_factory(FluentEntity fluent) {
        return FieldSpec.builder(fluent.wrapperFactory(), fluent.lowerNoSuffix() + "Default",
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$T.INSTANCE", fluent.wrapperFactory())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}