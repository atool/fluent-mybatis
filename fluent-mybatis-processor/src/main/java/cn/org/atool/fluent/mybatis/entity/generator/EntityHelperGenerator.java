package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.IEntityHelper;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Type;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.HashMap;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_EntityHelper;

public class EntityHelperGenerator extends AbstractGenerator {
    public static String getClassName(FluentEntityInfo fluent) {
        return fluent.getClassName() + Suffix_EntityHelper;
    }

    public static String getPackageName(FluentEntityInfo fluent) {
        return fluent.getEntityPack();
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    public EntityHelperGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "Entity帮助类";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder
            .addSuperinterface(parameterizedType(ClassName.get(IEntityHelper.class), fluent.className()))
            .addMethod(this.m_toEntityMap())
            .addMethod(this.m_toColumnMap())
            .addMethod(this.m_entity())
            .addMethod(this.m_copy());
    }

    /**
     * public static Map<String, Object> toEntityMap(Entity entity)
     *
     * @return
     */
    private MethodSpec m_toEntityMap() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toEntityMap")
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("Entity对象转换为HashMap，key值是对象属性")
            .addParameter(fluent.className(), "entity")
            .returns(this.parameterizedType(Map.class, String.class, Object.class))
            .addStatement("Map<String, Object> map = new $T<>()", HashMap.class);
        for (FieldColumn fc : fluent.getFields()) {
            String getMethod = fc.getMethodName();
            if (fc.isPrimitive()) {
                builder.addCode("map.put($T.$L.name, entity.$L());\n",
                    MappingGenerator.className(fluent), fc.getProperty(), getMethod);
            } else {
                builder.addCode("if (entity.$L() != null) {\n", getMethod);
                builder.addCode("\tmap.put($T.$L.name, entity.$L());\n",
                    MappingGenerator.className(fluent), fc.getProperty(), getMethod);
                builder.addCode("}\n");
            }
        }
        return builder.addStatement("return map").build();
    }

    /**
     * public static Map<String, Object> toColumnMap(Entity entity)
     *
     * @return
     */
    private MethodSpec m_toColumnMap() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toColumnMap")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addJavadoc("Entity对象转换为HashMap，key值是数据库字段")
            .addParameter(fluent.className(), "entity")
            .returns(this.parameterizedType(Map.class, String.class, Object.class))
            .addStatement("Map<String, Object> map = new $T<>()", HashMap.class);
        for (FieldColumn fc : fluent.getFields()) {
            String getMethod = fc.getMethodName();
            if (fc.isPrimitive()) {
                builder.addCode("map.put($T.$L.column, entity.$L());\n",
                    MappingGenerator.className(fluent), fc.getProperty(), getMethod);
            } else {
                builder.addCode("if (entity.$L() != null) {\n", getMethod);
                builder.addCode("\tmap.put($T.$L.column, entity.$L());\n",
                    MappingGenerator.className(fluent), fc.getProperty(), getMethod);
                builder.addCode("}\n");
            }
        }
        return builder.addStatement("return map").build();
    }

    /**
     * public static Entity entity(Map<String, Object> map)
     *
     * @return
     */
    private MethodSpec m_entity() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("entity")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addJavadoc("map对应属性值设置到Entity对象中")
            .addParameter(this.parameterizedType(Map.class, String.class, Object.class), "map")
            .returns(fluent.className())
            .addStatement("$T entity = new $T()", fluent.className(), fluent.className());
        for (FieldColumn fc : fluent.getFields()) {
            String setMethod = fc.setMethodName();

            builder.addCode("if (map.containsKey($T.$L.name)) {\n",
                MappingGenerator.className(fluent), fc.getProperty());
            builder.addCode("\tentity.$L(($T) map.get($T.$L.name));\n",
                setMethod, fc.getJavaType(), MappingGenerator.className(fluent), fc.getProperty());
            builder.addCode("}\n");
        }
        return builder.addStatement("return entity").build();
    }

    /**
     * public static Entity copy(Entity entity)
     *
     * @return
     */
    private MethodSpec m_copy() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("copy")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addJavadoc("拷贝一个entity对象")
            .addParameter(fluent.className(), "entity")
            .returns(fluent.className())
            .addStatement("$T copy = new $T()", fluent.className(), fluent.className());
        builder.addCode("{\n");
        for (FieldColumn fc : fluent.getFields()) {
            builder.addStatement("\tcopy.$L(entity.$L())", fc.setMethodName(), fc.getMethodName());
        }
        builder.addCode("}\n");
        return builder.addStatement("return copy").build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}