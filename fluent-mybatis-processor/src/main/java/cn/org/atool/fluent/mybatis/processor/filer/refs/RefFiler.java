package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.*;
import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.*;

/**
 * AllRef 文件构造
 *
 * @author darui.wu
 */
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

        spec.addField(this.f_mappers())
            .addMethod(this.m_constructor())
            .addMethod(this.m_mappers())
            .addMethod(this.m_getMapper())
            .addMethod(this.m_defaultQuery())
            .addMethod(this.m_emptyQuery())
            .addMethod(this.m_defaultUpdater())
            .addMethod(this.m_emptyUpdater())
            .addMethod(this.m_mapping("byEntity", "byEntity"))
            .addMethod(this.m_mapping("byMapper", "byMapper"))
            .addMethod(this.m_allEntityClass())
            .addMethod(this.m_allMapperClass())
            .addMethod(this.m_initEntityMapper());

        spec.addType(this.class_field())
            .addType(this.class_query())
            .addType(this.class_setter());
    }

    private MethodSpec m_mapping(String method, String call) {
        return publicMethod(method, IMapping.class)
            .addModifiers(Modifier.FINAL)
            .addParameter(String.class, "clazz")
            .addStatement("return $T.$L(clazz)", QueryRefFiler.getClassName(), call)
            .build();
    }

    private FieldSpec f_mappers() {
        return FieldSpec.builder(MapperRefFiler.getClassName(), "mappers", PRIVATE_STATIC).build();
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
        return protectMethod("allEntityClass", CN_Set_ClassName)
            .addModifiers(Modifier.FINAL)
            .addStatement("return $T.All_Entity_Class", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_allMapperClass() {
        return publicMethod("allMapperClass", CN_Map_AMapping)
            .addModifiers(Modifier.FINAL)
            .addStatement("return $T.MAPPER_MAPPING", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_mappers() {
        return staticMethod("mapper", MapperRefFiler.getClassName())
            .beginControlFlow("if (mappers == null)")
            .addStatement("validateMapperFactory()")
            .endControlFlow()
            .addStatement("return mappers")
            .build();
    }

    private MethodSpec m_getMapper() {
        return protectMethod("getMapper", IRichMapper.class)
            .addModifiers(Modifier.FINAL)
            .addParameter(CN_Class_IEntity, "clazz")
            .addStatement("Class<? extends IEntity> entityClass = super.findFluentEntityClass(clazz)")
            .addStatement("return MapperRef.mapper(entityClass)")
            .build();
    }

    private MethodSpec m_initEntityMapper() {
        return protectMethod("initEntityMapper", (TypeName) null)
            .addModifiers(Modifier.FINAL)
            .addStatement("mappers = $T.instance(super.mapperFactory)", MapperRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_defaultQuery() {
        return publicMethod(M_DEFAULT_QUERY, IQuery.class)
            .addParameter(Class.class, "clazz")
            .addStatement("Class entityClass = this.findFluentEntityClass(clazz)")
            .addStatement("return QueryRef.$L(entityClass)", M_DEFAULT_QUERY)
            .build();
    }

    private MethodSpec m_emptyQuery() {
        return publicMethod(M_EMPTY_QUERY, IQuery.class)
            .addParameter(Class.class, "clazz")
            .addStatement("Class entityClass = this.findFluentEntityClass(clazz)")
            .addStatement("return QueryRef.$L(entityClass)", M_EMPTY_QUERY)
            .build();
    }

    private MethodSpec m_defaultUpdater() {
        return publicMethod(M_DEFAULT_UPDATER, IUpdate.class)
            .addParameter(Class.class, "clazz")
            .addStatement("Class entityClass = this.findFluentEntityClass(clazz)")
            .addStatement("return QueryRef.$L(entityClass)", M_DEFAULT_UPDATER)
            .build();
    }

    private MethodSpec m_emptyUpdater() {
        return publicMethod(M_EMPTY_UPDATER, IUpdate.class)
            .addParameter(Class.class, "klass")
            .addStatement("Class entityClass = this.findFluentEntityClass(klass)")
            .addStatement("return QueryRef.$L(entityClass)", M_EMPTY_UPDATER)
            .build();
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