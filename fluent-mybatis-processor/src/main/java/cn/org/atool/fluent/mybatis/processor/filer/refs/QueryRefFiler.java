package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_NOT_FLUENT_MYBATIS_EXCEPTION;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_HashMap_AMapping;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Map_AMapping;

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
        super.staticImport(builder);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_mapping(fluent));
        }
        spec.addField(this.f_allDefaults())
            .addField(this.f_allEntityClass())
            .addMethod(m_defaultQuery(false))
            .addMethod(m_emptyQuery(false))
            .addMethod(m_defaultUpdater(false))
            .addMethod(m_emptyUpdater(false))
            .addMethod(this.m_mapping());
    }

    private FieldSpec f_mapping(FluentEntity fluent) {
        return FieldSpec.builder(fluent.entityKit(), fluent.lowerNoSuffix(),
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$T.Kit", fluent.entityKit())
            .build();
    }

    public static MethodSpec m_defaultQuery(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("defaultQuery")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(IQuery.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("Class entityClass = this.findFluentEntityClass(clazz)")
                .addStatement("return $T.defaultQuery(entityClass)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz实体对应的默认Query实例")
                .addStatement("\treturn mapping(clazz).defaultQuery()");
        }
        return spec.build();
    }

    public static MethodSpec m_emptyQuery(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("emptyQuery")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(IQuery.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("Class entityClass = this.findFluentEntityClass(clazz)")
                .addStatement("return $T.emptyQuery(entityClass)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz实体对应的空Query实例")
                .addStatement("\treturn mapping(clazz).query()");
        }
        return spec.build();
    }

    public static MethodSpec m_defaultUpdater(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("defaultUpdater")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(IUpdate.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("Class entityClass = this.findFluentEntityClass(clazz)")
                .addStatement("return $T.defaultUpdater(entityClass)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz实体对应的默认Updater实例")
                .addStatement("\treturn mapping(clazz).defaultUpdater()");
        }
        return spec.build();
    }

    public static MethodSpec m_emptyUpdater(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("emptyUpdater")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(IUpdate.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("Class entityClass = this.findFluentEntityClass(clazz)")
                .addStatement("return $T.emptyUpdater(entityClass)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz实体对应的空Updater实例")
                .addStatement("\treturn mapping(clazz).updater()");
        }
        return spec.build();
    }

    private MethodSpec m_mapping() {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("mapping")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(AMapping.class);

        spec.addModifiers(Modifier.STATIC)
            .addCode("if (ENTITY_DEFAULTS.containsKey(clazz)) {\n")
            .addStatement("\treturn ENTITY_DEFAULTS.get(clazz)")
            .addCode("}\n")
            .addStatement("throw $L(clazz)", M_NOT_FLUENT_MYBATIS_EXCEPTION);

        return spec.build();
    }

    private CodeBlock m_initEntityDefaults() {
        List<CodeBlock> list = new ArrayList<>();
        list.add(CodeBlock.of("new $T() {\n", CN_HashMap_AMapping));
        list.add(CodeBlock.of("\t{\n"));
        for (FluentEntity fluent : FluentList.getFluents()) {
            list.add(CodeBlock.of("\t\tthis.put($T.class, $L);\n", fluent.entity(), fluent.lowerNoSuffix()));
        }
        list.add(CodeBlock.of("\t}\n}"));
        return CodeBlock.join(list, "");
    }

    private FieldSpec f_allDefaults() {
        return FieldSpec.builder(CN_Map_AMapping, "ENTITY_DEFAULTS",
            Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .initializer(this.m_initEntityDefaults())
            .build();
    }

    private FieldSpec f_allEntityClass() {
        return FieldSpec.builder(parameterizedType(Set.class, Class.class), "All_Entity_Class",
            Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .initializer("$T.unmodifiableSet(ENTITY_DEFAULTS.keySet())", Collections.class)
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