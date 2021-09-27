package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.IRef;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.functions.FormFunction;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.RE_byEntity;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.RE_byMapper;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Map_AMapping;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Set_ClassName;
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
            .addMethod(this.m_mapperMapping())
            .addMethod(this.m_getMapper())
            .addMethod(this.m_mapping(RE_byEntity))
            .addMethod(this.m_mapping(RE_byMapper))
            .addMethod(this.m_allEntityClass())
            .addMethod(this.m_initEntityMapper());

        spec.addType(this.class_field())
            .addType(this.class_query())
            .addType(this.type_form());
    }

    private MethodSpec m_mapping(String method) {
        return protectMethod(method, IMapping.class)
            .addModifiers(Modifier.FINAL)
            .addParameter(String.class, "clazz")
            .addStatement("return $T.$L(clazz)", QueryRefFiler.getClassName(), method)
            .build();
    }

    private FieldSpec f_mappers() {
        return FieldSpec.builder(MapperRefFiler.getClassName(), "mappers", Modifier.PROTECTED).build();
    }

    private MethodSpec m_allEntityClass() {
        return protectMethod("allEntityClass", CN_Set_ClassName)
            .addModifiers(Modifier.FINAL)
            .addStatement("return $T.All_Entity_Class", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_mapperMapping() {
        return publicMethod("mapperMapping", CN_Map_AMapping)
            .addModifiers(Modifier.FINAL)
            .addStatement("return $T.MAPPER_MAPPING", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_getMapper() {
        return protectMethod("mapper", IRichMapper.class)
            .addModifiers(Modifier.FINAL)
            .addParameter(String.class, "eClass")
            .addStatement("return MapperRef.mapper(eClass)")
            .build();
    }

    private MethodSpec m_initEntityMapper() {
        return protectMethod("initEntityMapper", (TypeName) null)
            .addModifiers(Modifier.FINAL)
            .addStatement("mappers = $T.instance(super.mapperFactory)", MapperRefFiler.getClassName())
            .build();
    }

    private TypeSpec class_field() {
        TypeSpec.Builder spec = TypeSpec.interfaceBuilder("Field")
            .addJavadoc("所有Entity FieldMapping引用")
            .addModifiers(PUBLIC_STATIC);
        for (FluentEntity fluent : FluentList.getFluents()) {
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

    private TypeSpec class_query() {
        return TypeSpec.classBuilder("Query")
            .addModifiers(PUBLIC_STATIC_FINAL)
            .superclass(QueryRefFiler.getClassName())
            .build();
    }

    private TypeSpec type_form() {
        TypeSpec.Builder spec = TypeSpec.interfaceBuilder("Form")
            .addModifiers(PUBLIC_STATIC)
            .addJavadoc("所有Entity Form Setter引用");
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_formSetter(fluent));
        }
        return spec.build();
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
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}