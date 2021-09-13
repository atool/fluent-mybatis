package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.model.ClassMap;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler.PRIVATE_STATIC_FINAL;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.*;

/**
 * IMapperRef 文件构造
 *
 * @author darui.wu
 */
public class MapperRefFiler extends AbstractFile {
    private static final String MapperRef = "MapperRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), MapperRef);
    }

    public MapperRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = MapperRef;
        this.comment = "应用所有Mapper Bean引用";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addField(this.f_allMappers())
            .addField(this.f_instance());
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_mapper(fluent));
        }
        spec.addMethod(this.m_constructor())
            .addMethod(this.m_instance())
            .addMethod(this.m_mapper());
    }

    private MethodSpec m_mapper() {
        return MethodSpec.methodBuilder("mapper")
            .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC)
            .addParameter(CN_Class_IEntity, "entityClass")
            .returns(IRichMapper.class)
            .addStatement("return allMappers.get(entityClass)")
            .build();
    }

    private FieldSpec f_instance() {
        return FieldSpec.builder(getClassName(), "instance", Modifier.STATIC, Modifier.PRIVATE)
            .build();
    }

    private FieldSpec f_allMappers() {
        return FieldSpec.builder(parameterizedType(CN_ClassMap, FM_IRichMapper),
                "allMappers", PRIVATE_STATIC_FINAL)
            .initializer("new $T<>()", ClassMap.class)
            .build();
    }

    private FieldSpec f_mapper(FluentEntity fluent) {
        return FieldSpec.builder(fluent.mapper(), fluent.lowerNoSuffix() + "Mapper",
            Modifier.PUBLIC, Modifier.FINAL).build();
    }

    private MethodSpec m_constructor() {
        MethodSpec.Builder spec = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PRIVATE)
            .addParameter(FM_MapperFactory, "factory");

        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addStatement("this.$LMapper = factory.getBean($T.class)",
                fluent.lowerNoSuffix(), fluent.mapper());
        }
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addStatement("allMappers.put($T.class, this.$LMapper)",
                fluent.entity(), fluent.lowerNoSuffix());
        }
        spec.addStatement("allMappers.unmodified()");
        return spec.build();
    }

    private MethodSpec m_instance() {
        return MethodSpec.methodBuilder("instance")
            .addModifiers(Modifier.FINAL, Modifier.STATIC, Modifier.PUBLIC, Modifier.SYNCHRONIZED)
            .addParameter(FM_MapperFactory, "factory")
            .returns(getClassName())
            .beginControlFlow("if (instance == null)")
            .addStatement("instance = new MapperRef(factory)")
            .endControlFlow()
            .addStatement("return instance")
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