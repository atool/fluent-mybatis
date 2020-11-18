package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import cn.org.atool.generator.util.ClassNames;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.HashMap;

import static cn.org.atool.generator.util.ClassNames.*;

/**
 * IMapperRef 文件构造
 *
 * @author darui.wu
 */
public class MapperRefFiler extends AbstractFile {
    private static String MapperRef = "MapperRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), MapperRef);
    }

    public MapperRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = MapperRef;
        this.comment = "应用所有Mapper Bean引用";
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addField(this.f_allMappers());
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_mapper(fluent));
        }
        spec.addMethod(this.m_constructor())
            .addMethod(this.m_mapper())
            .addMethod(this.m_allEntityClass());
    }

    private MethodSpec m_mapper() {
        return MethodSpec.methodBuilder("mapper")
            .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
            .addParameter(CN_Class_IEntity, "entityClass")
            .returns(IRichMapper.class)
            .addStatement("return allMappers.get(entityClass)")
            .build();
    }

    private MethodSpec m_allEntityClass() {
        return MethodSpec.methodBuilder("allEntityClass")
            .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
            .returns(parameterizedType(CN_Set, CN_Class_IEntity))
            .addStatement("return allMappers.keySet()")
            .build();
    }

    private FieldSpec f_allMappers() {
        return FieldSpec.builder(
            parameterizedType(CN_Map, CN_Class_IEntity, FM_IRichMapper),
            "allMappers", Modifier.FINAL, Modifier.STATIC, Modifier.PRIVATE)
            .initializer("new $T<>()", HashMap.class)
            .build();
    }

    private FieldSpec f_mapper(FluentEntity fluent) {
        return FieldSpec.builder(fluent.mapper(), fluent.lowerNoSuffix() + "Mapper",
            Modifier.PUBLIC, Modifier.FINAL).build();
    }

    private MethodSpec m_constructor() {
        MethodSpec.Builder spec = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
            .addParameter(ClassNames.Spring_BeanFactory, "factory");

        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addStatement("this.$LMapper = factory.getBean($T.class)",
                fluent.lowerNoSuffix(), fluent.mapper());
        }
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addStatement("allMappers.put($T.class, this.$LMapper);",
                fluent.entity(), fluent.lowerNoSuffix());
        }
        return spec.build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}