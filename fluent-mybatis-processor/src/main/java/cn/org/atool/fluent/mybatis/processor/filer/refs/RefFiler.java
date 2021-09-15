package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler.PUBLIC_STATIC_FINAL;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.*;
import static cn.org.atool.fluent.mybatis.processor.filer.refs.QueryRefFiler.*;

/**
 * AllRef 文件构造
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public class RefFiler extends AbstractFile {
    private static final String Ref = "Ref";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), Ref);
    }

    public RefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = Ref;
        this.comment = "应用所有Mapper Bean引用";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(IRef.class)
            .addModifiers(Modifier.ABSTRACT);

        spec.addField(f_mappers())
            .addMethod(this.m_constructor())
            .addMethod(this.m_mappers())
            .addMethod(this.m_getMapper())
            .addMethod(m_defaultQuery(true))
            .addMethod(m_emptyQuery(true))
            .addMethod(m_defaultUpdater(true))
            .addMethod(m_emptyUpdater(true))
            .addMethod(this.m_mapping("byEntity", IMapping.class, "byEntity"))
            .addMethod(this.m_mapping("byMapper", IMapping.class, "byMapper"))
            .addMethod(this.m_mapping("defaults", BaseDefaults.class, "byEntity"))
            .addMethod(this.m_allEntityClass())
            .addMethod(this.m_allMapperClass())
            .addMethod(this.m_initEntityMapper());

        spec.addType(this.class_field())
            .addType(this.class_query())
            .addType(this.class_setter());
    }

    private MethodSpec m_mapping(String method, Class rClass, String call) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder(method)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(rClass);
        spec.addAnnotation(Override.class)
            .addStatement("return $T.$L(clazz)", QueryRefFiler.getClassName(), call);

        return spec.build();
    }

    private FieldSpec f_mappers() {
        return FieldSpec.builder(MapperRefFiler.getClassName(), "mappers",
            Modifier.PRIVATE, Modifier.STATIC).build();
    }

    private MethodSpec m_constructor() {
        MethodSpec.Builder spec = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC);
        if (isBlank(FluentList.getDbType())) {
            spec.addStatement("super.setDefaultDbType(null)");
        } else {
            spec.addStatement("super.setDefaultDbType($T.$L)", DbType.class, FluentList.getDbType());
        }
        return spec.build();
    }
    private MethodSpec m_allEntityClass() {
        return MethodSpec.methodBuilder("allEntityClass")
            .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
            .addAnnotation(Override.class)
            .returns(CN_Set_ClassName)
            .addStatement("return $T.All_Entity_Class", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_allMapperClass() {
        return MethodSpec.methodBuilder("allMapperClass")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotation(Override.class)
            .returns(CN_Map_AMapping)
            .addStatement("return $T.MAPPER_MAPPING", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_mappers() {
        return MethodSpec.methodBuilder("mapper")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(MapperRefFiler.getClassName())
            .beginControlFlow("if (mappers == null)")
            .addStatement("validateMapperFactory()")
            .endControlFlow()
            .addStatement("return mappers")
            .build();
    }

    private MethodSpec m_getMapper() {
        return MethodSpec.methodBuilder("getMapper")
            .addModifiers(Modifier.PROTECTED, Modifier.FINAL)
            .addParameter(CN_Class_IEntity, "clazz")
            .returns(IRichMapper.class)
            .addStatement("Class<? extends IEntity> entityClass = super.findFluentEntityClass(clazz)")
            .addStatement("return MapperRef.mapper(entityClass)")
            .build();
    }

    private MethodSpec m_initEntityMapper() {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("initEntityMapper")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.FINAL, Modifier.PROTECTED);
        spec.addStatement("mappers = $T.instance(super.mapperFactory)", MapperRefFiler.getClassName());
        return spec.build();
    }

    private TypeSpec class_field() {
        return TypeSpec.classBuilder("Field")
            .addModifiers(PUBLIC_STATIC_FINAL)
            .addSuperinterface(FieldRefFiler.getClassName())
            .build();
    }

    private TypeSpec class_query() {
        return TypeSpec.classBuilder("Query")
            .addModifiers(PUBLIC_STATIC_FINAL)
            .superclass(QueryRefFiler.getClassName())
            .build();
    }

    private TypeSpec class_setter() {
        return TypeSpec.classBuilder("Forms")
            .addModifiers(PUBLIC_STATIC_FINAL)
            .addSuperinterface(FormRefFiler.getClassName())
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