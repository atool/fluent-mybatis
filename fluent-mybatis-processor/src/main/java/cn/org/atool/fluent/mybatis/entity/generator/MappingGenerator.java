package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.entity.EntityKlass;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public class MappingGenerator extends AbstractGenerator {

    public static String getClassName(EntityKlass entityKlass) {
        return entityKlass.getNoSuffix() + "Mapping";
    }

    public static String getPackageName(EntityKlass entityKlass) {
        return entityKlass.getPackageName("helper");
    }

    public static ClassName className(EntityKlass entityKlass) {
        return ClassName.get(getPackageName(entityKlass), getClassName(entityKlass));
    }

    public MappingGenerator(TypeElement curElement, EntityKlass entityKlass) {
        super(curElement, entityKlass);
        this.packageName = getPackageName(entityKlass);
        this.klassName = getClassName(entityKlass);
        this.comment = "Entity类字段和表结构映射";
    }

    @Override
    public void build(TypeSpec.Builder builder) {
        builder.addField(this.f_Table_Name());
        builder.addField(this.f_Entity_Name());
        this.entityKlass.getFields().stream()
            .forEach(field -> {
                builder.addField(f_Field(field));
            });
        builder.addField(this.f_Property2Column());
        builder.addField(this.f_ALL_COLUMNS());
    }

    private FieldSpec f_Field(FieldColumn fc) {
        return FieldSpec.builder(FieldMapping.class,
            fc.getProperty(), Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("实体属性 : 数据库字段 映射\n $L : $L", fc.getProperty(), fc.getColumn())
            .initializer("new FieldMapping($S, $S)", fc.getProperty(), fc.getColumn())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return true;
    }

    private FieldSpec f_Table_Name() {
        return FieldSpec.builder(String.class, "Table_Name", Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
            .initializer("$S", entityKlass.getTableName())
            .addJavadoc(super.codeBlock("表名称"))
            .build();
    }

    private FieldSpec f_Entity_Name() {
        return FieldSpec.builder(String.class, "Entity_Name", Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
            .initializer("$S", entityKlass.getClassName())
            .addJavadoc(super.codeBlock("Entity名称"))
            .build();
    }

    private FieldSpec f_Property2Column() {
        String statement = this.entityKlass.getFields().stream()
            .map(FieldColumn::getProperty)
            .map(field -> String.format("\t\tthis.put(%s.name, %s.column);", field, field))
            .collect(joining("\n"));

        return FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, String.class),
            "Property2Column", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("实例属性和数据库字段对应表")
            .initializer(codeBlock(
                CodeBlock.of("new $T<String, String>() {", HashMap.class),
                CodeBlock.of("  {"),
                CodeBlock.of(statement),
                CodeBlock.of("  }"),
                CodeBlock.of("}")
            ))
            .build();
    }

    private FieldSpec f_ALL_COLUMNS() {
        String statement = this.entityKlass.getFields().stream()
            .map(FieldColumn::getProperty)
            .map(field -> String.format("\t\tthis.add(%s.column);", field))
            .collect(joining("\n"));

        return FieldSpec.builder(ParameterizedTypeName.get(Set.class, String.class),
            "ALL_COLUMNS", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("数据库所有字段列表")
            .initializer(codeBlock(
                CodeBlock.of("new $T<String>() {", HashSet.class),
                CodeBlock.of("  {"),
                CodeBlock.of(statement),
                CodeBlock.of("  }"),
                CodeBlock.of("}")
            ))
            .build();
    }
}