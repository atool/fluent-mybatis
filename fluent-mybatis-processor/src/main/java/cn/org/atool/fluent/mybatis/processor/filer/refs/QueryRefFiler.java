package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.RE_byEntity;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.RE_byMapper;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Map_AMapping;
import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.*;

public class QueryRefFiler extends AbstractFile {
    private static final String QueryRef = "QueryRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), QueryRef);
    }

    public QueryRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = QueryRef;
        this.comment = "构造Entity对应的default query\n更新器工厂类单例引用";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(MybatisUtil.class, M_NOT_FLUENT_MYBATIS_EXCEPTION);
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_mapping(fluent));
        }
        spec.addField(this.f_allDefaults())
            .addField(this.f_allMappers())
            .addField(this.f_allEntityClass())
            .addMethod(this.m_defaultQuery())
            .addMethod(this.m_emptyQuery())
            .addMethod(this.m_defaultUpdater())
            .addMethod(this.m_emptyUpdater())
            .addMethod(this.m_mapping(RE_byEntity, "ENTITY_MAPPING"))
            .addMethod(this.m_mapping(RE_byMapper, "MAPPER_MAPPING"));
    }

    private FieldSpec f_mapping(FluentEntity fluent) {
        return FieldSpec.builder(fluent.entityMapping(), fluent.lowerNoSuffix(), PUBLIC_STATIC_FINAL)
            .initializer("$T.MAPPING", fluent.entityMapping())
            .build();
    }

    private MethodSpec m_defaultQuery() {
        return staticMethod(M_DEFAULT_QUERY, IQuery.class)
            .addParameter(Class.class, "clazz")
            .addJavadoc("返回clazz实体对应的默认Query实例")
            .addStatement("\treturn byEntity(clazz.getName()).$L()", M_DEFAULT_QUERY)
            .build();
    }

    private MethodSpec m_emptyQuery() {
        return staticMethod(M_EMPTY_QUERY, IQuery.class)
            .addParameter(Class.class, "clazz")
            .addJavadoc("返回clazz实体对应的空Query实例")
            .addStatement("\treturn byEntity(clazz.getName()).$L()", M_EMPTY_QUERY)
            .build();
    }

    private MethodSpec m_defaultUpdater() {
        return staticMethod(M_DEFAULT_UPDATER, IUpdate.class)
            .addParameter(Class.class, "clazz")
            .addJavadoc("返回clazz实体对应的默认Updater实例")
            .addStatement("\treturn byEntity(clazz.getName()).$L()", M_DEFAULT_UPDATER)
            .build();
    }

    private MethodSpec m_emptyUpdater() {
        return staticMethod(M_EMPTY_UPDATER, IUpdate.class)
            .addParameter(Class.class, "clazz")
            .addJavadoc("返回clazz实体对应的空Updater实例")
            .addStatement("\treturn byEntity(clazz.getName()).$L()", M_EMPTY_UPDATER)
            .build();
    }

    private MethodSpec m_mapping(String method, String mapping) {
        return staticMethod(method, AMapping.class)
            .addParameter(String.class, "className")
            .addCode("if ($L.containsKey(className)) {\n", mapping)
            .addStatement("\treturn $L.get(className)", mapping)
            .addCode("}\n")
            .addStatement("throw $L(className)", M_NOT_FLUENT_MYBATIS_EXCEPTION)
            .build();
    }

    private FieldSpec f_allDefaults() {
        FieldSpec.Builder spec = FieldSpec.builder(CN_Map_AMapping, "ENTITY_MAPPING", PRIVATE_STATIC_FINAL);

        List<CodeBlock> list = new ArrayList<>();
        list.add(CodeBlock.of("new $T()", CN_Map_AMapping));
        for (FluentEntity fluent : FluentList.getFluents()) {
            list.add(CodeBlock.of(".put($T.class,  $L)", fluent.entity(), fluent.lowerNoSuffix()));
        }
        list.add(CodeBlock.of(".unmodified()"));
        return spec.initializer(CodeBlock.join(list, "\n\t")).build();
    }

    private FieldSpec f_allMappers() {
        FieldSpec.Builder spec = FieldSpec.builder(CN_Map_AMapping, "MAPPER_MAPPING", PUBLIC_STATIC_FINAL);

        List<CodeBlock> list = new ArrayList<>();
        list.add(CodeBlock.of("new $T()", CN_Map_AMapping));
        for (FluentEntity fluent : FluentList.getFluents()) {
            list.add(CodeBlock.of(".put($T.class, $L)", fluent.mapper(), fluent.lowerNoSuffix()));
        }
        list.add(CodeBlock.of(".unmodified()"));
        return spec.initializer(CodeBlock.join(list, "\n\t")).build();
    }

    private FieldSpec f_allEntityClass() {
        return FieldSpec.builder(parameterizedType(Set.class, String.class), "All_Entity_Class", PUBLIC_STATIC_FINAL)
            .initializer("$T.unmodifiableSet(ENTITY_MAPPING.keySet())", Collections.class)
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