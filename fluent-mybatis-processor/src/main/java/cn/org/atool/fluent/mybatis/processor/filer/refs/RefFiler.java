package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.crud.BaseDefaults;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Class_IEntity;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Set_Class;
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
            .addMethod(this.m_mapping("mapping", IMapping.class))
            .addMethod(this.m_mapping("defaults", BaseDefaults.class))
            .addMethod(this.m_allEntityClass())
            .addMethod(this.m_initEntityMapper())
        ;

        spec.addType(this.class_field())
            .addType(this.class_query())
            .addType(this.class_setter());
    }

    private MethodSpec m_mapping(String method, Class rClass) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder(method)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(rClass);
        spec.addAnnotation(Override.class)
            .addStatement("return $T.mapping(clazz)", QueryRefFiler.getClassName());

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
            .returns(CN_Set_Class)
            .addStatement("return $T.All_Entity_Class", QueryRefFiler.getClassName())
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
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(FieldRefFiler.getClassName())
            .build();
    }

    private TypeSpec class_query() {
        return TypeSpec.classBuilder("Query")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(QueryRefFiler.getClassName())
            .build();
    }

    private TypeSpec class_setter() {
        return TypeSpec.classBuilder("Form")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
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