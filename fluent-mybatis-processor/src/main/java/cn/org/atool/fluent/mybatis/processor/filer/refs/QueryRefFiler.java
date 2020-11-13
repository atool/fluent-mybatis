package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.*;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.processor.base.MethodName.M_NOT_FLUENT_MYBATIS_EXCEPTION;

public class QueryRefFiler extends AbstractFile {
    private static String QueryRef = "QueryRef";

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
        builder.addStaticImport(EntityRefs.class, M_NOT_FLUENT_MYBATIS_EXCEPTION);
        super.staticImport(builder);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_factory(fluent));
        }
        spec.addField(this.f_allQuerySupplier())
            .addStaticBlock(this.m_initSupplier())
            .addMethod(m_defaultQuery(false))
            .addMethod(m_defaultUpdater(false))
            .addMethod(m_setEntityByDefault(false))
            .addMethod(m_findDefaultGetter(false));
    }

    private FieldSpec f_factory(FluentEntity fluent) {
        return FieldSpec.builder(fluent.wrapperFactory(), fluent.lowerNoSuffix(),
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$T.INSTANCE", fluent.wrapperFactory())
            .build();
    }

    public static MethodSpec m_defaultQuery(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("defaultQuery")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(IQuery.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("return $T.defaultQuery(clazz)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz实体对应的默认Query实例")
                .addStatement("\treturn findDefaultGetter(clazz).defaultQuery()");
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
                .addStatement("return $T.defaultUpdater(clazz)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz实体对应的默认Query实例")
                .addStatement("\treturn findDefaultGetter(clazz).defaultUpdater()");
        }
        return spec.build();
    }

    public static MethodSpec m_setEntityByDefault(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("setEntityByDefault")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .addParameter(IEntity.class, "entity");
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("$T.setEntityByDefault(clazz, entity)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("按默认值设置entity属性")
                .addStatement("\tfindDefaultGetter(clazz).setEntityByDefault(entity)");
        }
        return spec.build();
    }

    public static MethodSpec m_findDefaultGetter(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("findDefaultGetter")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(IDefaultGetter.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("return $T.findDefaultGetter(clazz)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addCode("if (Supplier.containsKey(clazz)) {\n")
                .addStatement("\treturn Supplier.get(clazz)")
                .addCode("}\n")
                .addStatement("throw $L(clazz)", M_NOT_FLUENT_MYBATIS_EXCEPTION);
        }
        return spec.build();
    }

    private CodeBlock m_initSupplier() {
        List<CodeBlock> list = new ArrayList<>();
        for (FluentEntity fluent : FluentList.getFluents()) {
            list.add(CodeBlock.of("Supplier.put($T.class, $L);\n", fluent.entity(), fluent.lowerNoSuffix()));
        }
        return CodeBlock.join(list, "");
    }

    private FieldSpec f_allQuerySupplier() {
        return FieldSpec.builder(parameterizedType(Map.class, Class.class, IDefaultGetter.class), "Supplier",
            Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
            .initializer("new $T<>()", HashMap.class)
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