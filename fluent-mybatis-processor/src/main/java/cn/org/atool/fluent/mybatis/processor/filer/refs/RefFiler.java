package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.functions.FormFunction;
import cn.org.atool.fluent.mybatis.processor.entity.EntityRefMethod;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.refs.ARef;
import cn.org.atool.fluent.mybatis.spring.IMapperFactory;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Map_AMapping;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Set_ClassName;
import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.*;

/**
 * Ref 文件构造
 *
 * @author darui.wu
 */
public class RefFiler extends AbstractFile {

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), Suffix_Ref);
    }

    public RefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = Suffix_Ref;
        this.comment = "" +
            "\n o - 查询器，更新器工厂类单例引用" +
            "\n o - 应用所有Mapper Bean引用" +
            "\n o - Entity关联对象延迟加载查询实现";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(ARef.class)
            .addModifiers(Modifier.FINAL)
            .addMethod(this.m_mapperMapping())
            .addMethod(this.m_getMapper())
            .addMethod(this.m_mapping(RE_byEntity))
            .addMethod(this.m_mapping(RE_byMapper))
            .addMethod(this.m_allEntityClass())
            .addMethod(this.m_initialize())
            .addType(this.type_field())
            .addType(this.type_query())
            .addType(this.type_form());
    }

    private MethodSpec m_mapping(String method) {
        return protectMethod(method, AMapping.class)
            .addParameter(String.class, "clazz")
            .addStatement("return $T.$L(clazz)", QueryRefFiler.getClassName(), method)
            .build();
    }

    private MethodSpec m_allEntityClass() {
        return protectMethod("allEntityClass", CN_Set_ClassName)
            .addStatement("return $T.All_Entity_Class", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_mapperMapping() {
        return protectMethod("mapperMapping", CN_Map_AMapping)
            .addStatement("return $T.MAPPER_MAPPING", QueryRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_getMapper() {
        return protectMethod("mapper", IRichMapper.class)
            .addParameter(String.class, "eClass")
            .addStatement("return MapperRef.mapper(eClass)")
            .build();
    }

    private MethodSpec m_initialize() {
        MethodSpec.Builder spec = protectMethod("initialize", (TypeName) null)
            .addParameter(IMapperFactory.class, "factory")
            .addStatement("$T.instance(factory)", MapperRefFiler.getClassName())
            .addStatement("IEntityRelation relation = (IEntityRelation) factory.getRelation()");
        boolean hasAbstract = false;
        List<CodeBlock> codes = new ArrayList<>();
        for (FluentEntity fluent : FluentList.getFluents()) {
            for (EntityRefMethod refMethod : fluent.getRefMethods()) {
                if (refMethod.isAbstractMethod()) {
                    hasAbstract = true;
                }
                String methodName = refMethod.getRefMethod(fluent);
                codes.add(CodeBlock.of("this.put(relation::$L);", methodName));
            }
        }
        spec.beginControlFlow("if (relation == null)");
        if (hasAbstract) {
            spec.addStatement("throw new RuntimeException($S)", "IEntityRelation must be implemented and added to spring management.");
        } else {
            spec.addStatement("relation = new IEntityRelation() {}");
        }
        spec.endControlFlow();
        spec.addCode(CodeBlock.join(codes, "\n"));
        return spec.build();
    }

    private TypeSpec type_field() {
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

    private TypeSpec type_query() {
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