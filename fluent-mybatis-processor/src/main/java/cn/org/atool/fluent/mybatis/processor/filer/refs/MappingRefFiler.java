package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.EntityRefs;
import cn.org.atool.fluent.mybatis.base.IMapping;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IMappingRef 文件构造
 *
 * @author darui.wu
 */
public class MappingRefFiler extends AbstractFile {
    private static String MappingRef = "MappingRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), MappingRef);
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(EntityRefs.class, "notFluentMybatisException");
        super.staticImport(builder);
    }

    public MappingRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = MappingRef;
        this.comment = "Entity所有Mapping引用";
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addField(this.f_allMappings())
            .addStaticBlock(this.m_initMapping())
            .addMethod(m_findColumnByField(false))
            .addMethod(m_findPrimaryColumn(false));
    }

    private CodeBlock m_initMapping() {
        List<CodeBlock> list = new ArrayList<>();
        for (FluentEntity fluent : FluentList.getFluents()) {
            list.add(CodeBlock.of("Mappings.put($T.class, new $T() {});\n", fluent.entity(), fluent.mapping()));
        }
        return CodeBlock.join(list, "");
    }

    public static MethodSpec m_findPrimaryColumn(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("findPrimaryColumn")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .returns(String.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("return $T.findPrimaryColumn(clazz)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz属性对应数据库主键字段名称")
                .addCode("if (Mappings.containsKey(clazz)) {\n")
                .addStatement("\treturn Mappings.get(clazz).findPrimaryColumn()")
                .addCode("}\n")
                .addStatement("throw notFluentMybatisException(clazz)");
        }
        return spec.build();
    }

    public static MethodSpec m_findColumnByField(boolean isRef) {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("findColumnByField")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(Class.class, "clazz")
            .addParameter(String.class, "field")
            .returns(String.class);
        if (isRef) {
            spec.addAnnotation(Override.class)
                .addStatement("return $T.findColumnByField(clazz, field)", getClassName());
        } else {
            spec.addModifiers(Modifier.STATIC)
                .addJavadoc("返回clazz属性field对应的数据库字段名称")
                .addCode("if (Mappings.containsKey(clazz)) {\n")
                .addStatement("\treturn Mappings.get(clazz).findColumnByField(field)")
                .addCode("}\n")
                .addStatement("throw notFluentMybatisException(clazz)");
        }
        return spec.build();
    }

    private FieldSpec f_allMappings() {
        return FieldSpec.builder(parameterizedType(Map.class, Class.class, IMapping.class), "Mappings",
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