package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldType;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.*;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Helper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Mapping;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NEWLINE;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Optional_Mapping;
import static java.util.stream.Collectors.joining;

/**
 * Mapping代码生成
 *
 * @author wudarui
 */
public class MappingFiler extends AbstractFiler {

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Mapping;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Helper);
    }

    public MappingFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "Entity类字段和表结构映射";
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(Optional.class, "of");
    }

    @Override
    public void build(TypeSpec.Builder spec) {
        spec.addSuperinterface(IMapping.class)
            .addField(this.f_instance())
            .addField(this.f_Table_Name())
            .addField(this.f_Entity_Name());
        this.fluent.getFields().forEach(field -> spec.addField(f_Field(field)));
        spec.addField(this.f_Property2Column())
            .addField(this.f_Column2Mapping())
            .addField(this.f_ALL_COLUMNS())
            .addField(this.f_ALL_JOIN_COLUMNS())
            .addMethod(this.m_findColumnByField())
            .addMethod(this.m_findField());
    }

    private MethodSpec m_findField() {
        MethodSpec.Builder spec = super.publicMethod("findField", true, CN_Optional_Mapping)
            .addParameter(FieldType.class, "type")
            .addModifiers(Modifier.DEFAULT)
            .addCode("switch (type) {\n");
        if (fluent.getPrimary() != null) {
            spec.addCode("\tcase PRIMARY_ID:\n")
                .addStatement("\t\treturn of($L)", fluent.getPrimary().getName());
        }
        if (notBlank(fluent.getLogicDelete())) {
            spec.addCode("\tcase LOGIC_DELETED:\n")
                .addStatement("\t\treturn of($L)", fluent.getLogicDelete());
        }
        if (notBlank(fluent.getVersionField())) {
            spec.addCode("\tcase LOCK_VERSION:\n")
                .addStatement("\t\treturn of($L)", fluent.getVersionField());
        }
        return spec.addCode("\tdefault:\n")
            .addStatement("\t\treturn of(null)")
            .addCode("}")
            .build();
    }

    private MethodSpec m_findColumnByField() {
        return super.publicMethod("findColumnByField", true, String.class)
            .addParameter(String.class, "field")
            .addModifiers(Modifier.DEFAULT)
            .addStatement("return Property2Column.get(field)")
            .build();
    }

    private FieldSpec f_Field(CommonField fc) {
        FieldSpec.Builder spec = FieldSpec.builder(FieldMapping.class,
            fc.getName(), Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .addJavadoc("实体属性 : 数据库字段 映射\n $L : $L", fc.getName(), fc.getColumn());
        if (fc.getTypeHandler() == null) {
            return spec.initializer("new FieldMapping($S, $S)", fc.getName(), fc.getColumn())
                .build();
        } else {
            return spec.initializer("new FieldMapping($S, $S, $T.class, $T.class)",
                fc.getName(), fc.getColumn(), fc.getJavaType(), fc.getTypeHandler())
                .build();
        }
    }

    @Override
    protected boolean isInterface() {
        return true;
    }

    private FieldSpec f_Table_Name() {
        return FieldSpec.builder(String.class, "Table_Name", Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
            .initializer("$S", fluent.getTableName())
            .addJavadoc(super.codeBlock("表名称"))
            .build();
    }

    private FieldSpec f_Entity_Name() {
        return FieldSpec.builder(String.class, "Entity_Name", Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
            .initializer("$S", fluent.getClassName())
            .addJavadoc(super.codeBlock("Entity名称"))
            .build();
    }

    private FieldSpec f_Column2Mapping() {
        String statement = this.fluent.getFields().stream()
            .map(CommonField::getName)
            .map(field -> String.format("\t\tthis.put(%s.column, %s);", field, field))
            .collect(joining(NEWLINE));

        return FieldSpec.builder(ParameterizedTypeName.get(Map.class, String.class, FieldMapping.class),
            "Column2Mapping", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("数据库字段对应的FieldMapping")
            .initializer(codeBlock(
                CodeBlock.of("new $T<String, $T>() {", HashMap.class, FieldMapping.class),
                CodeBlock.of("  {"),
                CodeBlock.of(statement),
                CodeBlock.of("  }"),
                CodeBlock.of("}")
            ))
            .build();
    }

    private FieldSpec f_instance() {
        return FieldSpec.builder(fluent.mapping(), "MAPPING", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .initializer("new $T(){}", fluent.mapping())
            .build();
    }

    private FieldSpec f_Property2Column() {
        String statement = this.fluent.getFields().stream()
            .map(CommonField::getName)
            .map(field -> String.format("\t\tthis.put(%s.name, %s.column);", field, field))
            .collect(joining(NEWLINE));

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
        String statement = this.fluent.getFields().stream()
            .map(CommonField::getName)
            .map(field -> String.format("\t\t%s.column", field))
            .collect(joining(",\n"));

        return FieldSpec.builder(ParameterizedTypeName.get(List.class, String.class),
            "ALL_COLUMNS", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("数据库所有字段列表")
            .initializer(codeBlock(
                CodeBlock.of("$T.asList(", Arrays.class),
                CodeBlock.of(statement),
                CodeBlock.of(")")
            ))
            .build();
    }

    private FieldSpec f_ALL_JOIN_COLUMNS() {
        return FieldSpec.builder(String.class,
            "ALL_JOIN_COLUMNS", Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .addJavadoc("数据库所有字段列表用逗号分隔")
            .initializer("String.join($S, ALL_COLUMNS)", ", ")
            .build();
    }
}